package com.example.panther.project_softspec

import android.provider.BaseColumns

/**
 * Created by panther on 24/5/2018 AD.
 */
object DB {

    /* Inner class that defines the table contents */
    class HomeWorkEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "homework"
            val COLUMN_ID = "id"
            val COLUMN_NAME = "name"
            val COLUMN_NOTE = "note"
            val COLUMN_DATE = "date"
        }
    }
}