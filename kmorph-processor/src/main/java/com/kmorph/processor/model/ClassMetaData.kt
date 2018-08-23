package com.kmorph.processor.model

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

data class ClassMetaData(val classElement: TypeElement, val constructorElement: ExecutableElement?, val getterSetterAndFieldTransformMetaDataHashMap: HashMap<String, GetterSetterAndFieldTransformMetaData>, val noArgConstructorPresent: Boolean)