package com.kmorph


import com.kmorph.transformer.FieldTransformerContract

class DepartmentIdToNameTransformer : FieldTransformerContract<Int, String> {
    fun transformer(source: Int): String {
        return "Trojan Horse"
    }

    override fun transform(source: Int): String {
        return if (source == 2) "Tester" else "Developer"
    }

    fun reverseTransformer(target: String): Int {
        return -1
    }
    override fun reverseTransform(target: String): Int {
        return if (target == "Tester") 2 else 1
    }
}
