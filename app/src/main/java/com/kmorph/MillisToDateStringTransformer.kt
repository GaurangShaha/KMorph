package com.kmorph

import com.kmorph.transformer.FieldTransformerContract
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MillisToDateStringTransformer : FieldTransformerContract<String, Long> {
    private val DATE_FORMAT = "dd MMM yyyy"

    override fun reverseTransform(target: Long): String {
        return SimpleDateFormat(DATE_FORMAT).format(Date(target))
    }

    override fun transform(source: String): Long {
        return try {
            SimpleDateFormat(DATE_FORMAT).parse(source).time
        } catch (e: ParseException) {
            (-1).toLong()
        }
    }
}
