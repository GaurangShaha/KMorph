package com.kmorph.processor.generator

class FunctionSpecFactory {
    enum class FunctionSpecType {
        MORPH_TO_TARGET_USING_CONSTRUCTOR,
        MORPH_TO_TARGET_USING_GETTER_SETTER,
        MORPH_TO_SOURCE_USING_CONSTRUCTOR,
        MORPH_TO_SOURCE_USING_GETTER_SETTER,
    }

    fun getFunctionSpec(functionSpecType: FunctionSpecType): FunctionSpec {
        return when (functionSpecType) {
            FunctionSpecType.MORPH_TO_TARGET_USING_CONSTRUCTOR -> MorphToTargetUsingConstructorFunctionSpec()
            FunctionSpecType.MORPH_TO_TARGET_USING_GETTER_SETTER -> MorphToTargetUsingGetterSetterFunctionSpec()
            FunctionSpecType.MORPH_TO_SOURCE_USING_CONSTRUCTOR -> MorphToSourceUsingConstructorFunctionSpec()
            FunctionSpecType.MORPH_TO_SOURCE_USING_GETTER_SETTER -> MorphToSourceUsingGetterSetterFunctionSpec()
        }
    }
}