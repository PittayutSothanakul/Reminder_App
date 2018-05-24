package com.example.panther.project_softspec

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var homeworkDBHelper : databaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeworkDBHelper = databaseHelper(this)
    }

    fun addHomework(v:View){
        var id = this.idField.text.toString()
        var name = this.homeworkField.text.toString()
        var note = this.noteField.text.toString()
        var date  = this.dateField.text.toString()
//        var result = homeworkDBHelper.insertHomework(homeworkModel(id = id,homework = name,note = note, date = date))
        var result = homeworkDBHelper.insertHomework(homeworkModel(id = id,homework = name,note = note ))
        //clear all edittext s
        this.noteField.setText("")
        this.homeworkField.setText("")
        this.idField.setText("")
        this.dateField.setText("")
        this.textview_result.text = "Added homework : "+result
        this.ll_entries.removeAllViews()
    }

    fun deleteHomework(v:View){
        var id = this.idField.text.toString()
        val result = homeworkDBHelper.deleteHomework(id)
        this.textview_result.text = "Deleted homework : "+result
        this.ll_entries.removeAllViews()
    }

    fun showAllHomeworks(v:View){
        var homeworks = homeworkDBHelper.readAllHomeworks()
        this.ll_entries.removeAllViews()
        homeworks.forEach {
            var tv_homework = TextView(this)
            tv_homework.textSize = 25F
            tv_homework.text = it.id.toString()+ "   "+it.homework.toString() + "   " + it.note.toString()+ "   "
//            tv_homework.text = it.id.toString()+ "   "+it.homework.toString() + "   " + it.note.toString()+ "   "+ it.date.toString()
            this.ll_entries.addView(tv_homework)
        }
        this.textview_result.text = "Total " + homeworks.size + " homeworks"
    }
}

