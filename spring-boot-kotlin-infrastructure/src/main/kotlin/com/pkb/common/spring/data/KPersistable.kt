package com.pkb.common.spring.data

/**
 * See [JpaKotlinRepositoryFactoryBean]
 */
interface KPersistable<out ID> {
    val id: ID
    val new: Boolean
}