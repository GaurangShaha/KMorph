package com.kmorph


import com.kmorph.transformer.FieldTransformerContract

class DepartmentIdToNameTransformer : FieldTransformerContract<Int, String> {
    override fun transform(source: Int): String {
        return if (source == 2) "Tester" else "Developer"
    }

    override fun reverseTransform(target: String): Int {
        return if (target == "Tester") 2 else 1
    }
}
