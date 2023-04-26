package com.example.uwindsorcoursefinder

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.awaitAll
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.lang.Exception
import java.util.concurrent.TimeUnit

class Course {

    var code = ""
    var name = ""
    var description = ""
    var prerequisitesText = ""
    var prerequisites = mutableListOf<Course>()

    constructor(inputCode: String) {
        code = inputCode.replace(" ", "")

        if(code!="Elective"){
            CourseParser {
                //var undergradCal: Document = Jsoup.connect("https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/40a4d00a28a9d3e685257362006c8367!OpenDocument").get()
                //compsciCourses= Jsoup.connect("https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/fb6695172a9a1ba385257364004a8752!OpenDocument").get()
                Log.println(Log.DEBUG,"courseError",  code)

                var doc: Document
                if(code.contains("COMP")){
                    doc = compsciCourses
                }
                else{
                    doc = mathstatCourses
                }
                var courseNameData: Elements
                var codeNoPar = code.replace("\\(.*\\)".toRegex(), "");
                try{
                    courseNameData = doc.select("b:contains($code)")
                    if(courseNameData.text().split(" ").size>1){
                        name = courseNameData.text().substring(courseNameData.text().indexOf(" "), courseNameData.text().length)
                        var descriptionData = doc.select("b:contains($codeNoPar) + br + font")
                        description = descriptionData.text()
                    }
                    else{
                        courseNameData = doc.select("b:contains($codeNoPar) + b")
                        name = courseNameData.text()
                        var descriptionData = doc.select("b:contains($codeNoPar) + b + br + font")
                        description = descriptionData.text()
                    }
                    Log.println(Log.DEBUG,"courseError",  "Name is "+name+" compared to course code "+codeNoPar)
                    Log.println(Log.DEBUG,"courseError",  "Description is "+description)
                    /*if(descriptionData.text().split("(Prerequisite:").size>1){
                        prerequisitesText = descriptionData.text().split("(Prerequisite:")[1].split(".")[0]
                    }*/
                }
                catch (e:Exception){

                }


            }.execute()
        }

    }

    override fun toString(): String {
        return "$code - $name"
    }

}