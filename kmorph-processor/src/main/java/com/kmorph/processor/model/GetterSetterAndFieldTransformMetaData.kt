package com.kmorph.processor.model

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

data class GetterSetterAndFieldTransformMetaData(val fieldName: String, val getterElement: ExecutableElement?, val setterElement: ExecutableElement?, val fieldTransformerElement: TypeElement?)