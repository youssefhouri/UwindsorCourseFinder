package com.example.uwindsorcoursefinder

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ChooseYear : AppCompatActivity() {

    lateinit var lv_degreeList: ListView;
    lateinit var degreeListAdapter: ArrayAdapter<String>;
    lateinit var nextButton : Button
    lateinit var mainText: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_degree)

        var startup = true;

        mainText = findViewById<TextView>(R.id.main_text)
        nextButton = findViewById(R.id.next_butt)

        val message = intent.getStringExtra(EXTRA_MESSAGE)

        var sp: SharedPreferences = applicationContext.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        var year = sp.getString("CHOSEN_YEAR","")
        if (year != "" && year != null) {
            chosenYear = year.toInt()
        }

        if(chosenYear!=0){
            val intent = Intent(this, Recommended_tab::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }

        if(message == "com.example.UwindsorCourseFinder.Settings"){
            startup = false;
        }
        mainText.setText(R.string.choose_year)

        var yearStrings = resources.getStringArray(R.array.years)

        lv_degreeList = findViewById(R.id.degree_list)
        degreeListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, yearStrings)
        lv_degreeList.adapter = degreeListAdapter

        lv_degreeList.setOnItemClickListener { parent, view, position, id ->
            val element = degreeListAdapter.getItem(position) // The item that was clicked
            if (element != null) {
                chosenYear = element.split(" ").get(1).toInt()
            }
            Toast.makeText(this@ChooseYear, chosenYear.toString(), Toast.LENGTH_SHORT).show()
        }

        nextButton.setOnClickListener{

            var sp = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putString("CHOSEN_YEAR", chosenYear.toString())
            editor.commit()

            if(startup){
                val intent = Intent(this, Recommended_tab::class.java).apply {
                    putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
            }
            else{

            }
        }

    }
}