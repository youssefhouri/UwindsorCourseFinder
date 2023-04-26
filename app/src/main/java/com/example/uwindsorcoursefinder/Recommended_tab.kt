package com.example.uwindsorcoursefinder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class Recommended_tab : AppCompatActivity() {

    lateinit var lv_courseList1: ListView;
    lateinit var lv_courseList2: ListView;
    lateinit var courseListAdapter: ArrayAdapter<String>;
    lateinit var semester1text: TextView;
    lateinit var semester2text: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommended_tab)

        lv_courseList1 = findViewById(R.id.listView1)

        lv_courseList2 = findViewById(R.id.listView2)

        var resetButton = findViewById<Button>(R.id.resetPref)

        resetButton.setOnClickListener {
            var sp = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            var editor: SharedPreferences.Editor = sp.edit()
            editor.putString("CHOSEN_DEGREE", "")
            editor.commit()

            editor.putString("CHOSEN_YEAR", "")
            editor.commit()
        }
        CourseParser {
            //val p: Element = doc.select("font:containsOwn(Bachelor of )").first()
            var undergradCal: Document = Jsoup.connect("https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/40a4d00a28a9d3e685257362006c8367!OpenDocument").get()
            //var compsciCourses: Document = Jsoup.connect("https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/fb6695172a9a1ba385257364004a8752!OpenDocument").get()

            val pAll: Elements = undergradCal.select("b:contains(Bachelor of )")

            //var courseData = doc.select("i:contains(QUENCE) + br + i + font")
            //var degreeYearCourses = courseData.text().split(".")
            var baseHtmlJump = "+ br + i + font "
            var htmlJump = "+ br + i + font "
            for(yearIndex in 0..3){
                //Log.println(Log.DEBUG,"prints",  elementList.get(index).text())
                //Log.println(Log.DEBUG,"prints",  degreeYearCourses.get(index))
                var courseData = undergradCal.select("i:contains(QUENCE) $htmlJump")
                var courseDataText = courseData.text().replace(";", ".")
                courseDataText = courseDataText.replace("including ", "")
                courseDataText = courseDataText.replace("ten courses, ", "")
                courseDataText = courseDataText.replace("and ", "")
                var degreeYearCourses = courseDataText.split(".")

                Log.println(Log.DEBUG,"course",  "i:contains(QUENCE) $htmlJump")
                Log.println(Log.DEBUG,"course",  degreeYearCourses.toString())
                htmlJump+=baseHtmlJump
                for(degreeIndex in 0 until degrees.size){
                    var year = Year(degreeYearCourses.get(degreeIndex))
                    degrees.get(degreeIndex).setYear(yearIndex, year)
                    //degreeYearCourses = courseData.text().split(".")
                }
            }
            //Log.println(Log.DEBUG,"prints",  courseData.text())


            runOnUiThread {
                updateList()
                Thread.sleep(2000)
                updateList()
            }
        }.execute()


    }

    fun updateList(){
        var chosenDegree2: Degree = Degree("empty")
        for(degree in degrees){
            if(degree.name == chosenDegree){
                chosenDegree2 = degree
                Log.println(Log.DEBUG,"degree",  "Chosen degree is "+chosenDegree2.name)
            }
        }
        courseListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, chosenDegree2.getYears().get(chosenYear-1).getStringListSemester1())
        Log.println(Log.DEBUG,"degree",  "Chosen degree is "+chosenDegree2.getYears().get(chosenYear-1).getStringListSemester1().toString())

        for(i in chosenDegree2.getYears().get(chosenYear-1).semester1courses){
            Log.println(Log.DEBUG,"degree", "Course info: $i")
        }

        lv_courseList1.adapter = courseListAdapter
        var courseListAdapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, chosenDegree2.getYears().get(chosenYear-1).getStringListSemester2())
        Log.println(Log.DEBUG,"degree",  "Chosen degree is "+chosenDegree2.getYears().get(chosenYear-1).getStringListSemester2().toString())
        lv_courseList2.adapter = courseListAdapter2
        //Toast.makeText(this@ChooseDegree, p.toString(), Toast.LENGTH_LONG).show()
        //degreeListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, degreeStrings.distinct())
        //lv_degreeList.adapter = degreeListAdapter
        ListUtils.setDynamicHeight(lv_courseList1)
        ListUtils.setDynamicHeight(lv_courseList2)

        lv_courseList1.setOnItemClickListener { parent, view, position, id ->
            val element = courseListAdapter.getItem(position) // The item that was clicked
            var chosenCourse = element.toString()
            var foundCourse = chosenDegree2.getYears().get(chosenYear-1).semester1courses[position]
            val intent = Intent(this, CoursePage::class.java).apply {
                putExtra(EXTRA_MESSAGE, foundCourse.code+";;"+foundCourse.name+";;"+foundCourse.description)
            }
            startActivity(intent)
        }

        lv_courseList2.setOnItemClickListener { parent, view, position, id ->
            val element = courseListAdapter2.getItem(position) // The item that was clicked
            var chosenCourse = element.toString()
            var foundCourse = chosenDegree2.getYears().get(chosenYear-1).semester2courses[position]
            val intent = Intent(this, CoursePage::class.java).apply {
                putExtra(EXTRA_MESSAGE, foundCourse.code+";;"+foundCourse.name+";;"+foundCourse.description)
            }
            startActivity(intent)
        }
    }

    object ListUtils {
        fun setDynamicHeight(mListView: ListView) {
            val mListAdapter = mListView.adapter
                    ?: // when adapter is null
                    return
            var height = 0
            val desiredWidth = MeasureSpec.makeMeasureSpec(mListView.width, MeasureSpec.UNSPECIFIED)
            for (i in 0 until mListAdapter.count) {
                val listItem: View = mListAdapter.getView(i, null, mListView)
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
                height += listItem.getMeasuredHeight()
            }
            val params = mListView.layoutParams
            params.height = height + mListView.dividerHeight * (mListAdapter.count - 1)
            mListView.layoutParams = params
            mListView.requestLayout()
        }
    }
}