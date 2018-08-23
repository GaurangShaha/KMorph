package com.kmorph.processor.generator

import com.kmorph.processor.model.ClassMetaData
import com.squareup.kotlinpoet.FunSpec

interface FunctionSpec {
    fun generate(sourceClassMetaData: ClassMetaData, targetClassMetaData: ClassMetaData) : FunSpec
}