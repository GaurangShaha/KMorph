package com.kmorph.processor.util

import com.kmorph.annotation.FieldTransformer
import com.kmorph.annotation.MorphToField
import com.kmorph.processor.constant.Constant
import com.kmorph.processor.constant.Constant.GET
import com.kmorph.processor.constant.Constant.IS
import com.kmorph.processor.constant.Constant.SET
import com.kmorph.processor.exception.ValidationException
import com.kmorph.processor.model.CastingInfo
import com.kmorph.processor.model.ClassMetaData
import com.kmorph.processor.model.GetterSetterAndFieldTransformMetaData
import com.kmorph.processor.model.ParameterMatchType
import com.kmorph.transformer.FieldTransformerContract
import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Types
import kotlin.reflect.KClass


@Throws(ValidationException::class)
fun TypeElement.isValidElementForMorphToAnnotation(): Boolean {
    if (kind == ElementKind.INTERFACE) {
        throw ValidationException(String.format(Constant.ERROR_INTERFACE_FOUND, simpleName))
    }

    if (kind == ElementKind.ENUM) {
        throw ValidationException(String.format(Constant.ERROR_ENUM_FOUND, simpleName))
    }

    if (kind == ElementKind.ANNOTATION_TYPE) {
        throw ValidationException(String.format(Constant.ERROR_ANNOTATION_TYPE_FOUND, simpleName))
    }

    if (modifiers.contains(Modifier.PRIVATE)) {
        throw ValidationException(String.format(Constant.ERROR_PRIVATE_CLASS_FOUND, simpleName))
    }

    if (modifiers.contains(Modifier.PROTECTED)) {
        throw ValidationException(String.format(Constant.ERROR_PROTECTED_CLASS_FOUND, simpleName))
    }

    if (modifiers.contains(Modifier.ABSTRACT)) {
        throw ValidationException(String.format(Constant.ERROR_ABSTRACT_CLASS_FOUND, simpleName))
    }

    if (!modifiers.contains(Modifier.PUBLIC)) {
        throw ValidationException(String.format(Constant.ERROR_PUBLIC_CLASS_NOT_FOUND, simpleName))
    }

    return true
}

fun Element.noArgConstructorFound(): Boolean {
    var noArgConstructorFound = false
    ElementFilter.constructorsIn(enclosedElements).forEach {
        if (it.parameters.size == 0)
            noArgConstructorFound = true
    }
    return noArgConstructorFound
}


fun Element.getTypeElementFromAnnotationValue(kClass: KClass<*>, annotationParameterName: String? = null, typeUtils: Types): TypeElement? {
    getAnnotationValue(kClass, annotationParameterName)?.value?.let {
        return typeUtils.asElement(it as TypeMirror) as TypeElement
    }
    return null
}


fun Element.getAnnotationValue(kClass: KClass<*>, annotationParameterName: String? = null): AnnotationValue? {
    getAnnotationMirror(kClass)?.elementValues?.forEach {
        if (annotationParameterName == null) {
            return it.value
        } else if (it.key.simpleName.toString() == annotationParameterName) {
            return it.value
        }
    }
    return null
}

fun Element.getAnnotationMirror(kClass: KClass<*>): AnnotationMirror? {
    annotationMirrors.forEach {
        if (it.annotationType.toString() == kClass.java.name.toString()) {
            return it
        }
    }
    return null
}

fun TypeElement.extractClassMetaData(typeUtils: Types): ClassMetaData {
    return ClassMetaData(this, getConstructor(), getFieldAndGetterSetter(typeUtils), noArgConstructorFound())
}

private fun TypeElement.getFieldAndGetterSetter(typeUtils: Types): HashMap<String, GetterSetterAndFieldTransformMetaData> {
    val fieldAndGetterSetter = HashMap<String, GetterSetterAndFieldTransformMetaData>()

    ElementFilter.fieldsIn(enclosedElements).forEach {
        val getterElement = findGetterForField(it, this)
        val setterElement = findSetterForField(it, this)


        val morphingFieldName = it.getAnnotationValue(MorphToField::class)?.value ?: it.simpleName

        if (getterElement != null || setterElement != null)
            fieldAndGetterSetter[morphingFieldName.toString()] = GetterSetterAndFieldTransformMetaData(it.simpleName.toString(), getterElement, setterElement, getFieldTransformerTypeElement(it, typeUtils))
    }

    return fieldAndGetterSetter
}

private fun getFieldTransformerTypeElement(variableElement: VariableElement, typeUtils: Types): TypeElement? {
    val typeElement = variableElement.getTypeElementFromAnnotationValue(kClass = FieldTransformer::class, typeUtils = typeUtils)
    return if (typeElement?.isValidTypeElementForFieldTransformer() == true)
        typeElement
    else
        null
}

private fun TypeElement.isValidTypeElementForFieldTransformer(): Boolean {
    if (kind == ElementKind.INTERFACE) {
        throw ValidationException(String.format(Constant.ERROR_INTERFACE_USED_AS_FIELD_TRANSFORMER, simpleName))
    }

    if (kind == ElementKind.ENUM) {
        throw ValidationException(String.format(Constant.ERROR_ENUM_USED_AS_FIELD_TRANSFORMER, simpleName))
    }

    if (kind == ElementKind.ANNOTATION_TYPE) {
        throw ValidationException(String.format(Constant.ERROR_ANNOTATION_TYPE_AS_FIELD_TRANSFORMER, simpleName))
    }

    if (modifiers?.contains(Modifier.PRIVATE) != false) {
        throw ValidationException(String.format(Constant.ERROR_PRIVATE_CLASS_AS_FIELD_TRANSFORMER, simpleName))
    }

    if (modifiers?.contains(Modifier.PROTECTED) != false) {
        throw ValidationException(String.format(Constant.ERROR_PROTECTED_CLASS_AS_FIELD_TRANSFORMER, simpleName))
    }

    if (modifiers?.contains(Modifier.ABSTRACT) != false) {
        throw ValidationException(String.format(Constant.ERROR_ABSTRACT_CLASS_AS_FIELD_TRANSFORMER, simpleName))
    }

    if (!interfaces.toString().contains(FieldTransformerContract::class.qualifiedName!!)) {
        throw ValidationException(String.format(Constant.ERROR_TRANSFORMER_CLASS_NOT_IMPLEMENTED_FIELD_TRANSFORMER_CONTRACT, simpleName))
    }

    return true
}


fun findSetterForField(variableElement: VariableElement, typeElement: TypeElement): ExecutableElement? {
    ElementFilter.methodsIn(typeElement.enclosedElements).forEach {
        if (it.isValidSetterFor(variableElement) && it.parameters.size == 1 && it.modifiers.contains(Modifier.PUBLIC) && variableElement.asType().toString() == it.parameters[0].asType().toString()) {
            return it
        }
    }
    return null
}

fun findGetterForField(variableElement: VariableElement, typeElement: TypeElement): ExecutableElement? {
    ElementFilter.methodsIn(typeElement.enclosedElements).forEach {
        if (it.isValidGetterFor(variableElement) && it.parameters.size == 0 && it.modifiers.contains(Modifier.PUBLIC) && variableElement.asType().toString() == it.returnType.toString()) {
            return it
        }
    }
    return null
}

private fun ExecutableElement.isValidSetterFor(variableElement: VariableElement): Boolean {
    return simpleName.toString().replace(SET, "").equals(variableElement.simpleName.toString(), true)
}

private fun ExecutableElement.isValidGetterFor(variableElement: VariableElement): Boolean {
    return simpleName.toString().replace(GET, "").equals(variableElement.simpleName.toString(), true) || simpleName.toString().replace(IS, "").equals(variableElement.simpleName.toString(), true)
}

private fun TypeElement.getConstructor(): ExecutableElement? {
    var suitableConstructor: ExecutableElement? = null

    ElementFilter.constructorsIn(enclosedElements).forEach {
        if (it.parameters.size >= suitableConstructor?.parameters?.size ?: 0 && it.modifiers.contains(Modifier.PUBLIC)) {
            suitableConstructor = it
        }
    }

    return suitableConstructor
}

fun TypeMirror.hasCompatibleDataType(targetTypeMirror: TypeMirror): ParameterMatchType {
    if (toString() == targetTypeMirror.toString()) {
        return ParameterMatchType.SAME_CLASS
    }

    if (kind.isPrimitive && targetTypeMirror.kind.isPrimitive) {
        if (CastingInfo.upCastingHashMap[toString()]?.contains(targetTypeMirror.toString()) == true) {
            return ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_PRIMITIVE
        } else if (CastingInfo.downCastingHashMap[toString()]?.contains(targetTypeMirror.toString()) == true) {
            return ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_PRIMITIVE
        }
    }

    if ((kind.isPrimitive && targetTypeMirror.toString() == CastingInfo.primitiveWrapperClassHashMap[toString()]) || (targetTypeMirror.kind.isPrimitive && toString() == CastingInfo.primitiveWrapperClassHashMap[targetTypeMirror.toString()])) {
        return ParameterMatchType.SAME_CLASS
    }

    if (!kind.isPrimitive && !targetTypeMirror.kind.isPrimitive) {
        if (CastingInfo.upCastingHashMapForWrapperClass[toString()]?.contains(targetTypeMirror.toString()) == true) {
            return ParameterMatchType.UPCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS
        } else if (CastingInfo.downCastingHashMapForWrapperClass[toString()]?.contains(targetTypeMirror.toString()) == true) {
            return ParameterMatchType.DOWNCAST_NEEDED_WITH_BOTH_WRAPPER_CLASS
        }
    }

    return ParameterMatchType.NO_MATCH
}

fun TypeMirror.getPrimitiveDataType(): String {
    CastingInfo.primitiveWrapperClassHashMap.forEach {
        if (it.value == toString())
            return it.key
    }
    return toString()
}