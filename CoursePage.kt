package com.example.uwindsorcoursefinder

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CoursePage : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.course_page)

        //comp-1000;;name;;desc
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        var splitMessage = message?.split(";;")

        Log.println(Log.DEBUG,"page", "message is $message")
        var course_code = findViewById<TextView>(R.id.course_code_text)
        course_code.text = splitMessage?.get(1)
        var course_name = findViewById<TextView>(R.id.course_name_text)
        course_name.text = splitMessage?.get(0)
        var course_desc = findViewById<TextView>(R.id.course_desc_text)
        course_desc.text = splitMessage?.get(2)


    }

}