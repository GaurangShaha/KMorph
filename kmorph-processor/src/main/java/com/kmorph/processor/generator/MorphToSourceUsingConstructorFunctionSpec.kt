package com.kmorph.processor.generator

import com.kmorph.processor.constant.Constant
import com.kmorph.processor.model.ClassMetaData
import com.kmorph.processor.model.ParameterMatchType
import com.kmorph.processor.util.getPrimitiveDataType
import com.kmorph.processor.util.hasCompatibleDataType
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter

class MorphToSourceUsingConstructorFunctionSpec : FunctionSpec {
    override fun generate(sourceClassMetaData: ClassMetaData, targetClassMetaData: ClassMetaData): FunSpec {
        val morphToSourceStringBuilder = StringBuilder("return ${sourceClassMetaData.classElement.simpleName}(")
        var startUsingNamedArgument = false
        var firstVariableInConstructor = true
        sourceClassMetaData.constructorElement?.parameters?.forEach { it ->
            val probableMatchFromTarget = targetClassMetaData.getterSetterAndFieldTransformMetaDataHashMap[getKeyNameForTargetClass(it, sourceClassMetaData)]

            if (probableMatchFromTarget?.getterElement == null) {
                startUsingNamedArgument = true
                return@forEach
            }

            when (it.asType().hasCompatibleDataType(probableMatchFromTarget.getterElement.returnType)) {
                ParameterMatchType.SAME_CLASS -> {
                    if (!firstVariableInConstructor)
                        morphToSourceStringBuilder.append(", ")
                    if (startUsingNamedArgument)
                        morphToSourceStringBuilder.append("${it.simpleName} = ")
                    morphToSourceStringBuilder.append(probableMatchFromTarget.fieldName)
                    firstVariableInConstructor = false
                }
                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_PRIMITIVE, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_PRIMITIVE -> {
                    if (!firstVariableInConstructor)
                        morphToSourceStringBuilder.append(", ")

                    if (startUsingNamedArgument)
                        morphToSourceStringBuilder.append("${it.simpleName} = ")
                    morphToSourceStringBuilder.append("${probableMatchFromTarget.fieldName}.to${it.asType().toString().capitalize()}()")
                    firstVariableInConstructor = false
                }
                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS -> {
                    if (!firstVariableInConstructor)
                        morphToSourceStringBuilder.append(", ")

                    if (startUsingNamedArgument)
                        morphToSourceStringBuilder.append("${it.simpleName} = ")
                    morphToSourceStringBuilder.append("${probableMatchFromTarget.fieldName}?.to${it.asType().getPrimitiveDataType().capitalize()}()")
                    firstVariableInConstructor = false
                }
                else -> {
                    var isValidFieldTransformerFound = false
                    val transformerElement = sourceClassMetaData.getterSetterAndFieldTransformMetaDataHashMap[probableMatchFromTarget.fieldName]?.fieldTransformerElement
                    if (transformerElement != null) {
                        ElementFilter.methodsIn(transformerElement.enclosedElements).filter { it.simpleName.toString() == Constant.TRANSFORM || it.simpleName.toString() == Constant.REVERSE_TRANSFORM }.forEach { transformerMethod ->
                            when (it.asType().hasCompatibleDataType(transformerMethod.returnType)) {
                                ParameterMatchType.SAME_CLASS -> {
                                    if (!firstVariableInConstructor)
                                        morphToSourceStringBuilder.append(", ")

                                    if (startUsingNamedArgument)
                                        morphToSourceStringBuilder.append("${it.simpleName} = ")
                                    morphToSourceStringBuilder.append("${transformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromTarget.fieldName})")
                                    isValidFieldTransformerFound = true
                                    firstVariableInConstructor = false
                                }
                                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_PRIMITIVE, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_PRIMITIVE -> {
                                    if (!firstVariableInConstructor)
                                        morphToSourceStringBuilder.append(", ")

                                    if (startUsingNamedArgument)
                                        morphToSourceStringBuilder.append("${it.simpleName} = ")
                                    morphToSourceStringBuilder.append("${transformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromTarget.fieldName}.to${transformerMethod.parameters[0].asType().toString().capitalize()}())")
                                    isValidFieldTransformerFound = true
                                    firstVariableInConstructor = false
                                }
                                ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS, ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS -> {
                                    if (!firstVariableInConstructor)
                                        morphToSourceStringBuilder.append(", ")

                                    if (startUsingNamedArgument)
                                        morphToSourceStringBuilder.append("${it.simpleName} = ")
                                    morphToSourceStringBuilder.append("${transformerElement.qualifiedName}().${transformerMethod.simpleName}(${probableMatchFromTarget.fieldName}?.to${transformerMethod.parameters[0].asType().getPrimitiveDataType().capitalize()}())")
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
        morphToSourceStringBuilder.append(")")

        return FunSpec.builder("morphTo${sourceClassMetaData.classElement.simpleName}")
                .receiver(targetClassMetaData.classElement.asClassName())
                .returns(sourceClassMetaData.classElement.asClassName())
                .addStatement(morphToSourceStringBuilder.toString())
                .build()
    }

    private fun getKeyNameForTargetClass(variableElement: VariableElement, sourceClassMetaData: ClassMetaData): String? {
        sourceClassMetaData.getterSetterAndFieldTransformMetaDataHashMap.forEach {
            if (it.value.fieldName == variableElement.simpleName.toString()) {
                return it.key
            }
        }
        return variableElement.simpleName.toString()
    }
}