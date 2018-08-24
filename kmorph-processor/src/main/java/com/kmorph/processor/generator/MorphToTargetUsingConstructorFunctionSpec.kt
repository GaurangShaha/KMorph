package com.kmorph.processor.generator

import com.kmorph.processor.constant.Constant
import com.kmorph.processor.model.ClassMetaData
import com.kmorph.processor.model.ParameterMatchType
import com.kmorph.processor.util.getPrimitiveDataType
import com.kmorph.processor.util.hasCompatibleDataType
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.util.ElementFilter

class MorphToTargetUsingConstructorFunctionSpec : FunctionSpec {
    override fun generate(sourceClassMetaData: ClassMetaData, targetClassMetaData: ClassMetaData): FunSpec {
        val morphToTargetStringBuilder = StringBuilder("return ${targetClassMetaData.classElement.simpleName}(")
        var startUsingNamedArgument = false
        var firstVariableInConstructor = true

        targetClassMetaData.constructorElement?.parameters?.forEach { it ->
            val probableMatchFromSource = sourceClassMetaData.getterSetterAndFieldTransformMetaDataHashMap[it.simpleName.toString()]

            if (probableMatchFromSource?.getterElement == null) {
                startUsingNamedArgument = true
                return@forEach
            }


            when (it.asType().hasCompatibleDataType(probableMatchFromSource.getterElement.returnType)) {
                ParameterMatchType.SAME_CLASS -> {
                    if (!firstVariableInConstructor)
                        morphToTargetStringBuilder.append(", ")
                    if (startUsingNamedArgument)
                        morphToTargetStringBuilder.append("${it.simpleName} = ")
                    morphToTargetStringBuilder.append(probableMatchFromSource.fieldName)
                    firstVariableInConstructor = false
                }
                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_PRIMITIVE, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_PRIMITIVE -> {
                    if (!firstVariableInConstructor)
                        morphToTargetStringBuilder.append(", ")
                    if (startUsingNamedArgument)
                        morphToTargetStringBuilder.append("${it.simpleName} = ")
                    morphToTargetStringBuilder.append("${probableMatchFromSource.fieldName}.to${it.asType().toString().capitalize()}()")
                    firstVariableInConstructor = false
                }
                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS -> {
                    if (!firstVariableInConstructor)
                        morphToTargetStringBuilder.append(", ")
                    if (startUsingNamedArgument)
                        morphToTargetStringBuilder.append("${it.simpleName} = ")
                    morphToTargetStringBuilder.append("${probableMatchFromSource.fieldName}?.to${it.asType().getPrimitiveDataType().capitalize()}()")
                    firstVariableInConstructor = false
                }
                else -> {
                    var isValidFieldTransformerFound = false
                    if (probableMatchFromSource.fieldTransformerElement != null) {
                        ElementFilter.methodsIn(probableMatchFromSource.fieldTransformerElement.enclosedElements).filter { it.simpleName.toString() == Constant.TRANSFORM || it.simpleName.toString() == Constant.REVERSE_TRANSFORM }.forEach { transformerMethod ->
                            when (it.asType().hasCompatibleDataType(transformerMethod.returnType)) {
                                ParameterMatchType.SAME_CLASS -> {
                                    if (!firstVariableInConstructor)
                                        morphToTargetStringBuilder.append(", ")
                                    if (startUsingNamedArgument)
                                        morphToTargetStringBuilder.append("${it.simpleName} = ")
                                    morphToTargetStringBuilder.append("${probableMatchFromSource.fieldTransformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromSource.fieldName})")
                                    isValidFieldTransformerFound = true
                                    firstVariableInConstructor = false
                                }
                                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_PRIMITIVE, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_PRIMITIVE -> {
                                    if (!firstVariableInConstructor)
                                        morphToTargetStringBuilder.append(", ")
                                    if (startUsingNamedArgument)
                                        morphToTargetStringBuilder.append("${it.simpleName} = ")
                                    morphToTargetStringBuilder.append("${probableMatchFromSource.fieldTransformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromSource.fieldName}.to${transformerMethod.parameters[0].asType().toString().capitalize()}())")
                                    isValidFieldTransformerFound = true
                                    firstVariableInConstructor = false
                                }
                                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS -> {
                                    if (!firstVariableInConstructor)
                                        morphToTargetStringBuilder.append(", ")
                                    if (startUsingNamedArgument)
                                        morphToTargetStringBuilder.append("${it.simpleName} = ")
                                    morphToTargetStringBuilder.append("${probableMatchFromSource.fieldTransformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromSource.fieldName}?.to${transformerMethod.parameters[0].asType().getPrimitiveDataType().capitalize()}())")
                                    isValidFieldTransformerFound = true
                                    firstVariableInConstructor = false
                                }
                                else -> {
                                }
                            }
                        }
                    }

                    if (!isValidFieldTransformerFound) {
                        startUsingNamedArgument = true
                        return@forEach
                    }
                }
            }


        }
        morphToTargetStringBuilder.append(")")

        return FunSpec.builder("morphTo${targetClassMetaData.classElement.simpleName}")
                .receiver(sourceClassMetaData.classElement.asClassName())
                .returns(targetClassMetaData.classElement.asClassName())
                .addStatement(morphToTargetStringBuilder.toString())
                .build()
    }
}