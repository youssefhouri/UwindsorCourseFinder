package com.example.uwindsorcoursefinder

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

var chosenDegree = ""
var chosenYear = 0
var EXTRA_MESSAGE = "com.example.UwindsorCourseFinder.Startup"
var degrees = mutableListOf<Degree>()
var compsciCourses:Document = Document("")
var undergradCal = Document("")
var mathstatCourses:Document = Document("")
