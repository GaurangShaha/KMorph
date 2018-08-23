package com.kmorph.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class FieldTransformer(val value: KClass<*>)
