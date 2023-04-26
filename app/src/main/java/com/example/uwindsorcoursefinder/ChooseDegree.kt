package com.example.uwindsorcoursefinder

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.jsoup.select.QueryParser


class ChooseDegree : AppCompatActivity() {

    lateinit var lv_degreeList: ListView;
    lateinit var degreeListAdapter: ArrayAdapter<String>;
    lateinit var nextButton : Button
    lateinit var mainText: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_degree)

        var degreeStrings = mutableListOf<String>()

        var startup = true;

        mainText = findViewById<TextView>(R.id.main_text)
        nextButton = findViewById(R.id.next_butt)

        val message = intent.getStringExtra(EXTRA_MESSAGE)

        var sp: SharedPreferences = applicationContext.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        chosenDegree = sp.getString("CHOSEN_DEGREE","").toString()

        if(chosenDegree!=""){
            val intent = Intent(this, ChooseYear::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }

        Toast.makeText(this@ChooseDegree, chosenDegree, Toast.LENGTH_SHORT).show()

        if(message.equals("com.example.UwindsorCourseFinder.Settings")){
            startup = false;
            mainText.setText(R.string.choose_degree_startup)
        }

        lv_degreeList = findViewById(R.id.degree_list)

        lv_degreeList.setOnItemClickListener { parent, view, position, id ->
            val element = degreeListAdapter.getItem(position) // The item that was clicked
            chosenDegree = element.toString()
            Toast.makeText(this@ChooseDegree, chosenDegree, Toast.LENGTH_SHORT).show()
        }

        nextButton.setOnClickListener{

            var sp = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor: Editor = sp.edit()
            editor.putString("CHOSEN_DEGREE", chosenDegree)
            editor.commit()

            if(startup){
                val intent = Intent(this, ChooseYear::class.java).apply {
                    putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
                //Toast.makeText(this@ChooseDegree, "next", Toast.LENGTH_SHORT).show()
            }
            else{

            }
        }

        CourseParser {
            //val path: URL = ClassLoader.getSystemResource("src/main/java/doc.html")
            //val input = File(path.toURI())
            //val input = File("/doc.html")
            //val doc: Document = Jsoup.parse(input, "UTF-8", "https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/40a4d00a28a9d3e685257362006c8367!OpenDocument\"")
            undergradCal = Jsoup.connect("https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/40a4d00a28a9d3e685257362006c8367!OpenDocument").get()
            compsciCourses = Jsoup.connect("https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/fb6695172a9a1ba385257364004a8752!OpenDocument").get()
            mathstatCourses = Jsoup.connect("https://web4.uwindsor.ca/units/registrar/calendars/undergraduate/cur.nsf/982f0e5f06b5c9a285256d6e006cff78/c4d12a22771cfa81852585310007c4bd!OpenDocument").get()

            //String webdata = ""
            //val el: Element = doc.select(":containsOwn(Bachelor)").first()
            val pAll: Elements = undergradCal.select("b:contains(Bachelor of )")

            var tempList = mutableListOf<String>()
            var elementList = mutableListOf<Element>()
            for(p2 in pAll.distinct()) {
                if (!p2.text().contains("Co-op") and !p2.text().contains("Graduates") and !p2.text().contains("Concurrent") and !p2.text().contains("Minor") and !p2.text().contains("Diploma")) {
                    //Log.println(Log.DEBUG,"prints",  p2.text())
                    var found = false
                    for (i in 0..elementList.size - 1) {
                        if (elementList.get(i).text() == p2.text()) {
                            //Log.println(Log.DEBUG, "prints", "Item was replaced")
                            elementList.set(i, p2)
                            found = true
                        }
                    }
                    if(found == false){
                        elementList.add(p2)
                    }
                    //degreeStrings.add(p2.text())
                }
            }

            var courseData = undergradCal.select("i:contains(QUENCE) + br + i + font")
            var degreeYearCourses = undergradCal.text().split(".")
            for(index in 0 until elementList.size){
                Log.println(Log.DEBUG,"prints",  elementList.get(index).text())
                Log.println(Log.DEBUG,"prints",  degreeYearCourses.get(index))
            }
            //Log.println(Log.DEBUG,"prints",  courseData.text())

            //Log.println(Log.DEBUG,"prints", elementList.size.toString())
            for(element in elementList){
                degrees.add(Degree(element.text()))
                //Log.println(Log.DEBUG,"prints", element.text())
                //Log.println(Log.DEBUG,"prints", element.)
                var courseData = undergradCal.select("i:contains(RECOMMENDED) + br + i + font")
                runOnUiThread{
                    //Toast.makeText(this@ChooseDegree, courseData.toString(), Toast.LENGTH_LONG).show()
                }
                //Log.println(Log.DEBUG,"prints",  courseData.get(1).text())
                if(courseData.size!=0){
                    //Log.println(Log.DEBUG,"prints",  courseData.next().next().next().text())
                }
                //degreeStrings.add(element.text())
            }
            //Log.println(Log.DEBUG,"prints", p2.text())
            //Log.println(Log.DEBUG,"prints", p2.attr("ada-og-font-size"))


            runOnUiThread {
                //Toast.makeText(this@ChooseDegree, p.toString(), Toast.LENGTH_LONG).show()
                for(degree in degrees){
                    degreeStrings.add(degree.getName())
                }
                degreeListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, degreeStrings.distinct())
                lv_degreeList.adapter = degreeListAdapter

            }
        }.execute()

    }

    fun selectNthElementAfter(origin: Element, query: String?, count: Int): Element? {
        var count = count
        var currentElement: Element? = origin
        val evaluator = QueryParser.parse(query)
        while (currentElement!!.nextElementSibling().also { currentElement = it } != null) {
            var `val` = 0
            if (currentElement!!.`is`(evaluator)) {
                if (--count == 0) return currentElement
                `val`++
            }
            val elems = currentElement!!.select(query)
            if (elems.size > `val`) {
                val childCount = elems.size - `val`
                val diff = count - childCount
                if (diff == 0) {
                    return elems.last()
                }
                if (diff > 0) {
                    count -= childCount
                }
                if (diff < 0) {
                    return elems[childCount + diff]
                }
            }
        }
        return if (origin.parent() != null && currentElement == null) {
            selectNthElementAfter(origin.parent(), query, count)
        } else currentElement
    }
}