package com.pkb.common.spring.data

import java.util.UUID
import javax.persistence.MappedSuperclass
import javax.persistence.PostLoad
import javax.persistence.PostPersist
import javax.persistence.Transient

/**
 * Base class for use with Hibernate entities in Kotlin that use a pre-generated UUID as their PK
 * See [JpaKotlinRepositoryFactoryBean] for more details
 */
@MappedSuperclass
abstract class EntityWithUUIDBase : KPersistable<UUID> {

    /**
     * All our entities have equality in terms of their id. Since unique ids are assigned
     * at the time of object creation rather than persistence, this is reliable no matter
     * what JPA status the entity has.
     */
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        //Since these are UUIDs we don't have to be type-specific here, they will be globally unique
        if (!EntityWithUUIDBase::class.java.isAssignableFrom(other.javaClass)) return false

        other as EntityWithUUIDBase

        if (id != other.id) return false

        return true
    }

    final override fun hashCode(): Int {
        return id.hashCode()
    }

    final override val new
        get() = !persisted

    @Transient
    private var persisted = false

    @PostLoad
    @PostPersist
    private fun setPersisted() {
        persisted = true
    }
}