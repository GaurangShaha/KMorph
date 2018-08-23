package com.kmorph.processor.generator

import com.kmorph.processor.constant.Constant
import com.kmorph.processor.model.ClassMetaData
import com.kmorph.processor.model.GetterSetterAndFieldTransformMetaData
import com.kmorph.processor.model.ParameterMatchType
import com.kmorph.processor.util.hasCompatibleDataType
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.util.ElementFilter

class MorphToTargetUsingGetterSetterFunctionSpec : FunctionSpec {
    override fun generate(sourceClassMetaData: ClassMetaData, targetClassMetaData: ClassMetaData): FunSpec {
        val morphToTargetFunSpecBuilder = FunSpec.builder("morphTo${targetClassMetaData.classElement.simpleName}")
                .receiver(sourceClassMetaData.classElement.asClassName())
                .returns(targetClassMetaData.classElement.asClassName())

        morphToTargetFunSpecBuilder.addStatement("val target = %T()", targetClassMetaData.classElement.asClassName())
        targetClassMetaData.getterSetterAndFieldTransformMetaDataHashMap.forEach {
            val probableMatchFromSource: GetterSetterAndFieldTransformMetaData? = sourceClassMetaData.getterSetterAndFieldTransformMetaDataHashMap[it.key]

            if (probableMatchFromSource?.getterElement == null || it.value.setterElement == null)
                return@forEach

            when (it.value.setterElement!!.parameters[0].asType().hasCompatibleDataType(probableMatchFromSource.getterElement.returnType)) {
                ParameterMatchType.SAME_CLASS -> morphToTargetFunSpecBuilder.addStatement("target.${it.value.fieldName} = ${probableMatchFromSource.fieldName}")
                ParameterMatchType.UPCAST_NEEDED, ParameterMatchType.DOWNCAST_NEEDED -> morphToTargetFunSpecBuilder.addStatement("target.${it.value.fieldName} = ${probableMatchFromSource.fieldName}.to${it.value.setterElement?.parameters?.get(0)?.asType().toString().capitalize()}()")
                else -> {
                    if (probableMatchFromSource.fieldTransformerElement != null) {
                        ElementFilter.methodsIn(probableMatchFromSource.fieldTransformerElement.enclosedElements).filter { it.simpleName.toString() == Constant.TRANSFORM || it.simpleName.toString() == Constant.REVERSE_TRANSFORM }.forEach { transformerMethod ->
                            when (it.value.setterElement!!.parameters[0].asType().hasCompatibleDataType(transformerMethod.returnType)) {
                                ParameterMatchType.SAME_CLASS -> morphToTargetFunSpecBuilder.addStatement("target.${it.value.fieldName} = ${probableMatchFromSource.fieldTransformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromSource.fieldName})")
                                ParameterMatchType.UPCAST_NEEDED, ParameterMatchType.DOWNCAST_NEEDED -> morphToTargetFunSpecBuilder.addStatement("target.${it.value.fieldName} = ${probableMatchFromSource.fieldTransformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromSource.fieldName}.to${it.value.setterElement?.parameters?.get(0)?.asType().toString().capitalize()}())")
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
        }
        morphToTargetFunSpecBuilder.addStatement("return target")
        return morphToTargetFunSpecBuilder.build()
    }
}