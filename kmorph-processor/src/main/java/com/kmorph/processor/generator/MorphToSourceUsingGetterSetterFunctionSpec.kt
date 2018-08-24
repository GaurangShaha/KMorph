package com.kmorph.processor.generator

import com.kmorph.processor.constant.Constant
import com.kmorph.processor.model.ClassMetaData
import com.kmorph.processor.model.GetterSetterAndFieldTransformMetaData
import com.kmorph.processor.model.ParameterMatchType.*
import com.kmorph.processor.util.getPrimitiveDataType
import com.kmorph.processor.util.hasCompatibleDataType
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.util.ElementFilter

class MorphToSourceUsingGetterSetterFunctionSpec : FunctionSpec {
    override fun generate(sourceClassMetaData: ClassMetaData, targetClassMetaData: ClassMetaData): FunSpec {
        val morphToSourceFunSpecBuilder = FunSpec.builder("morphTo${sourceClassMetaData.classElement.simpleName}")
                .receiver(targetClassMetaData.classElement.asClassName())
                .returns(sourceClassMetaData.classElement.asClassName())

        morphToSourceFunSpecBuilder.addStatement("val source = %T()", sourceClassMetaData.classElement.asClassName())
        sourceClassMetaData.getterSetterAndFieldTransformMetaDataHashMap.forEach {
            val probableMatchFromTarget: GetterSetterAndFieldTransformMetaData? = targetClassMetaData.getterSetterAndFieldTransformMetaDataHashMap[it.key]

            if (probableMatchFromTarget?.getterElement == null || it.value.setterElement == null)
                return@forEach

            when (it.value.setterElement!!.parameters[0].asType().hasCompatibleDataType(probableMatchFromTarget.getterElement.returnType)) {
                SAME_CLASS -> morphToSourceFunSpecBuilder.addStatement("source.${it.value.fieldName} = ${probableMatchFromTarget.fieldName}")
                UPCAST_NEEDED_WITH_BOTH_PRIMITIVE, DOWNCAST_NEEDED_WITH_BOTH_PRIMITIVE -> morphToSourceFunSpecBuilder.addStatement("source.${it.value.fieldName} = ${probableMatchFromTarget.fieldName}.to${it.value.setterElement?.parameters?.get(0)?.asType().toString().capitalize()}()")
                UPCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS, DOWNCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS -> morphToSourceFunSpecBuilder.addStatement("source.${it.value.fieldName} = ${probableMatchFromTarget.fieldName}?.to${it.value.setterElement?.parameters?.get(0)?.asType()?.getPrimitiveDataType()?.capitalize()}()")
                else -> {
                    if (it.value.fieldTransformerElement != null) {
                        ElementFilter.methodsIn(it.value.fieldTransformerElement!!.enclosedElements).filter { it.simpleName.toString() == Constant.TRANSFORM || it.simpleName.toString() == Constant.REVERSE_TRANSFORM }.forEach { transformerMethod ->
                            when (it.value.setterElement!!.parameters[0].asType().hasCompatibleDataType(transformerMethod.returnType)) {
                                SAME_CLASS -> morphToSourceFunSpecBuilder.addStatement("source.${it.value.fieldName} = ${it.value.fieldTransformerElement!!.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromTarget.fieldName})")
                                UPCAST_NEEDED_WITH_BOTH_PRIMITIVE, DOWNCAST_NEEDED_WITH_BOTH_PRIMITIVE -> morphToSourceFunSpecBuilder.addStatement("source.${it.value.fieldName} = ${it.value.fieldTransformerElement!!.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromTarget.fieldName}.to${it.value.setterElement?.parameters?.get(0)?.asType()?.getPrimitiveDataType()?.capitalize()}())")
                                UPCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS, DOWNCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS -> morphToSourceFunSpecBuilder.addStatement("source.${it.value.fieldName} = ${it.value.fieldTransformerElement!!.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromTarget.fieldName}?.to${it.value.setterElement?.parameters?.get(0)?.asType().toString().capitalize()}())")
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
        }
        morphToSourceFunSpecBuilder.addStatement("return source")

        return morphToSourceFunSpecBuilder.build()
    }
}
