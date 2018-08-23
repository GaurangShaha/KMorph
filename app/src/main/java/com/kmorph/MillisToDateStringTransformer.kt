package com.kmorph

import android.view.View
import com.kmorph.transformer.FieldTransformerContract
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MillisToDateStringTransformer : View.OnClickListener, FieldTransformerContract<String, Long> {
    override fun onClick(p0: View?) {

    }

    private val DATE_FORMAT = "dd MMM yyyy"

    override fun reverseTransform(source: Long): String {
        return SimpleDateFormat(DATE_FORMAT).format(Date(source))
    }

    override fun transform(target: String): Long {
        try {
            return SimpleDateFormat(DATE_FORMAT).parse(target).time
        } catch (e: ParseException) {
            return (-1).toLong()
        }

    }
}
