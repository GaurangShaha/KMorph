package com.kmorph.transformer

interface FieldTransformerContract<SOURCE, TARGET> {
    fun transform(source: SOURCE): TARGET

    fun reverseTransform(target: TARGET): SOURCE
}
