package com.example.uwindsorcoursefinder

import android.util.Log

class Year {

    var courses = mutableListOf<String>()
    //var semester1courses = Array(5){"Elective"}
    //var semester2courses = Array(5){"Elective"}

    var semester1courses = Array(5){Course("Elective")}
    var semester2courses = Array(5){Course("Elective")}

    constructor(courses: String) {
        //this.courses = courses
        var splitCourses = courses.split(",")
        var counter = 0

        //Log.println(Log.DEBUG,"courseTest",  splitCourses.toString() )

        Log.println(Log.DEBUG,"split",  splitCourses.toString())
        Log.println(Log.DEBUG,"split",  kotlin.math.ceil((splitCourses.size) / 2.0).toInt().toString())
        Log.println(Log.DEBUG,"split",  splitCourses.size.toString())

        for(index in 0 until kotlin.math.ceil(((splitCourses.size)) / 2.0).toInt()){
            semester1courses[index] = Course(splitCourses[counter])
            //Log.println(Log.DEBUG,"courseTest",  semester1courses[index].code )
            counter++
        }
        for(index in 0 until kotlin.math.floor(((splitCourses.size)) / 2.0).toInt()){
            //Log.println(Log.DEBUG,"courseTest",  semester2courses[index].code )
            semester2courses[index] = Course(splitCourses[counter])
            counter++
        }
    }

    fun getStringListSemester1(): Array<String> {
        var stringSemesterArray = Array(5){""}
        for(index in 0..4){
            stringSemesterArray[index] = semester1courses[index].toString()
        }
        return stringSemesterArray
    }

    fun getStringListSemester2(): Array<String> {
        var stringSemesterArray = Array(5){""}
        for(index in 0..4){
            stringSemesterArray[index] = semester2courses[index].toString()
        }
        return stringSemesterArray
    }

    fun getCourseSem1(code: String): Course {
        for(course in semester1courses){
            if(course.code == code){
                return course
            }
        }
        return Course("Elective")
    }

    fun getCourseSem2(code: String): Course {
        for(course in semester2courses){

            if(course.code.contains(code)){
                return course
            }
        }
        return Course("Elective")
    }

}