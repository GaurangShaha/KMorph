package com.kmorph.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MorphTo(val targetClass: KClass<*>)