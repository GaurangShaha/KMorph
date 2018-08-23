package com.kmorph

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.math.BigInteger
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val endDateCalender = Calendar.getInstance()
        endDateCalender.add(Calendar.YEAR, 5)

        val studentDAO = StudentDAO("Gaurang", "Shaha", BigInteger("127873871287382"), "9130517000", "gaurang.shaha@gmail.com", "Android Developer", 1, "John Doe", "Blue ridge, Hinjewadi, Pune", 3, true, System.currentTimeMillis(), endDateCalender.timeInMillis, "www.facebook.com/gaurang.shaha")
//        val studentDTO: StudentDTO = studentDAO.morphToStudentDTO()
    }
}


