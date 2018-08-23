package com.kmorph.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class MorphToField(val fieldName: String)