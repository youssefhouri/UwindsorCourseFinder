package com.example.uwindsorcoursefinder

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "points.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE COURSE_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, COURSE_CODE TEXT, COURSE_NAME TEXT, COURSE_DESC TEXT);"
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun addCourse(course: Course): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("COURSE_CODE", course.code)
        cv.put("COURSE_NAME", course.name)
        cv.put("COURSE_DESC", course.description)
        val insert = db.insert("COURSE_TABLE", null, cv)
        return insert == 1L
    }

    //int userID = cursor.getInt(0);
    val users: List<String>
        get() {
            val userlist: MutableList<String> = ArrayList()
            val queryString = "SELECT DISTINCT USER_NAME FROM POINT_TABLE"
            val db = readableDatabase
            val cursor = db.rawQuery(queryString, null)
            if (cursor != null && cursor.count > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        //int userID = cursor.getInt(0);
                        val userName = cursor.getString(0)
                        //int userPoints = cursor.getInt(2);
                        userlist.add(userName)
                    } while (cursor.moveToNext())
                }
            }
            cursor!!.close()
            db.close()
            return userlist
        }

    //int userID = cursor.getInt(0);
    val all: List<String>
        get() {
            val userlist: MutableList<String> = ArrayList()
            val queryString = "SELECT * FROM COURSE_TABLE ORDER BY USER_POINTS DESC"
            val db = readableDatabase
            val cursor = db.rawQuery(queryString, null)
            if (cursor != null && cursor.count > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        //int userID = cursor.getInt(0);
                        val userName = cursor.getString(1)
                        val userPoints = cursor.getInt(2)
                        val value = "user: $userName score: $userPoints"
                        userlist.add(value)
                    } while (cursor.moveToNext())
                }
            }
            cursor!!.close()
            db.close()
            return userlist
        }
}