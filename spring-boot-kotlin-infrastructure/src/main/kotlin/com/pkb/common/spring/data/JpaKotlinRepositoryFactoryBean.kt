package com.pkb.common.spring.data

import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.query.EscapeCharacter
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.mapping.context.MappingContext
import org.springframework.data.querydsl.EntityPathResolver
import org.springframework.data.querydsl.SimpleEntityPathResolver
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport
import org.springframework.lang.Nullable
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.metamodel.Metamodel


/**
 * Spring data repositories have a generic "save" method that abstracts the difference between insert and update. Internally, it works by
 * checking to see if the entity has already been persisted. The default strategy for this is simply to see if it has an id value or not,
 * which is fine for Hibernate- or DB-generated ids, but no good if you're using a UUID that is set up front before persistence. To get
 * round this, Spring lets you implement an interface [org.springframework.data.domain.Persistable] with your entities to specify explicitly
 * when an entity is "new". The Spring Data docs explicitly describe using this mechanism for exactly this use case. See [https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-persistence.saving-entites.strategies]
 *
 * Unfortunately, this interface doesn't play nicely with Kotlin entities, because it has a a getId method that
 * can't be overriden by a Kotlin property, so we'd have to either forgo Kotlin-style property access for all our ids, or given them so other
 * name. Since neither of these options are particularly nice, this class reimplements the standard Spring behaviour with a modified interface
 * more suitable for use with Kotlin objects.
 *
 * It would have been a lot neater where it not for the fact that [org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean] and
 * its superclasses are full of private members that make extension much harder. As a result I had to reimplement a bunch of simple methods
 * just to be able to change the class this instantiates to a trivial subclass. Oh well.
 */
class JpaKotlinRepositoryFactoryBean<T : Repository<S, ID>, S, ID>(clazz: Class<out T>) : TransactionalRepositoryFactoryBeanSupport<T, S, ID>(clazz) {

    @PersistenceContext
    lateinit var entityManager: EntityManager
    private lateinit var entityPathResolver: EntityPathResolver
    private var _escapeCharacter = EscapeCharacter.DEFAULT
    @Autowired
    var queryMethodFactory: JpaQueryMethodFactory? = null

    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#setMappingContext(org.springframework.data.mapping.context.MappingContext)
	 */
    public override fun setMappingContext(mappingContext: MappingContext<*, *>) {
        super.setMappingContext(mappingContext)
    }


    @Autowired
    fun setEntityPathResolver(resolver: ObjectProvider<EntityPathResolver>) {
        entityPathResolver = resolver.getIfAvailable { SimpleEntityPathResolver.INSTANCE }
    }

    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport#doCreateRepositoryFactory()
	 */
    override fun doCreateRepositoryFactory(): RepositoryFactorySupport {
        val jpaRepositoryFactory = JpaKotlinRepositoryFactory(entityManager)
        jpaRepositoryFactory.setEntityPathResolver(entityPathResolver)
        jpaRepositoryFactory.setEscapeCharacter(_escapeCharacter)
        if (queryMethodFactory != null) {
            jpaRepositoryFactory.setQueryMethodFactory(queryMethodFactory!!)
        }
        return jpaRepositoryFactory
    }


    fun setEscapeCharacter(escapeCharacter: Char) {
        this._escapeCharacter = EscapeCharacter.of(escapeCharacter)
    }

}

internal class JpaKotlinRepositoryFactory(private val em: EntityManager) : JpaRepositoryFactory(em) {

    @Suppress("UNCHECKED_CAST")
    override fun <T, ID> getEntityInformation(domainClass: Class<T>): JpaEntityInformation<T, ID> {
        if (KPersistable::class.java.isAssignableFrom(domainClass)) {
            val clazz: Class<KPersistable<ID>> = domainClass as Class<KPersistable<ID>>
            return JpaKPersistableEntityInformation(clazz, em.metamodel) as JpaEntityInformation<T, ID>
        }
        return super.getEntityInformation(domainClass)
    }
}


internal class JpaKPersistableEntityInformation<T : KPersistable<ID>, ID>(domainClass: Class<T>, metamodel: Metamodel) : JpaMetamodelEntityInformation<T, ID>(domainClass, metamodel) {
    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation#isNew(java.lang.Object)
	 */
    override fun isNew(entity: T): Boolean {
        return entity.new
    }

    /*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation#getId(java.lang.Object)
	 */
    @Nullable
    override fun getId(entity: T): ID {
        return entity.id
    }
}


