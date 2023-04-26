package com.example.uwindsorcoursefinder

import android.os.AsyncTask

class CourseParser(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}