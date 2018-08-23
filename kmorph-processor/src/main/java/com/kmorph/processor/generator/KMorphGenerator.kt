package com.kmorph.processor.generator

import com.kmorph.processor.constant.Constant.AUTO_GENERATE_CLASS_FROM_KMORPH_LIBRARY
import com.kmorph.processor.generator.FunctionSpecFactory.FunctionSpecType.*
import com.kmorph.processor.model.ClassMetaData
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.asClassName
import java.io.File

class KMorphGenerator(val generatedFilePath: String) {
    fun generate(sourceClassMetaData: ClassMetaData, targetClassMetaData: ClassMetaData) {
        val functionSpecFactory = FunctionSpecFactory()
        val file = FileSpec.builder(sourceClassMetaData.classElement.asClassName().packageName, String.format("${sourceClassMetaData.classElement.simpleName}Morpher"))
                .addComment(AUTO_GENERATE_CLASS_FROM_KMORPH_LIBRARY)
                .addFunction(functionSpecFactory.getFunctionSpec(if (targetClassMetaData.noArgConstructorPresent) MORPH_TO_TARGET_USING_GETTER_SETTER else MORPH_TO_TARGET_USING_CONSTRUCTOR).generate(sourceClassMetaData, targetClassMetaData))
                .addFunction(functionSpecFactory.getFunctionSpec(if (sourceClassMetaData.noArgConstructorPresent) MORPH_TO_SOURCE_USING_GETTER_SETTER else MORPH_TO_SOURCE_USING_CONSTRUCTOR).generate(sourceClassMetaData, targetClassMetaData))
                .build()

        file.writeTo(File(generatedFilePath))
    }
}



