package com.example.panther.project_softspec

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class databaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertHomework(homeworks: homeworkModel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DB.HomeWorkEntry.COLUMN_ID, homeworks.id)
        values.put(DB.HomeWorkEntry.COLUMN_NAME, homeworks.homework)
        values.put(DB.HomeWorkEntry.COLUMN_NOTE, homeworks.note)
//        values.put(DB.HomeWorkEntry.COLUMN_DATE , homeworks.date)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DB.HomeWorkEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteHomework(id: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DB.HomeWorkEntry.COLUMN_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id)
        // Issue SQL statement.
        db.delete(DB.HomeWorkEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readHomework(id: String): ArrayList<homeworkModel> {
        val homeworks = ArrayList<homeworkModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DB.HomeWorkEntry.TABLE_NAME + " WHERE " + DB.HomeWorkEntry.COLUMN_ID + "='" + id + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var note: String
        var date: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(DB.HomeWorkEntry.COLUMN_NAME))
                note = cursor.getString(cursor.getColumnIndex(DB.HomeWorkEntry.COLUMN_NOTE))
//                date = cursor.getString(cursor.getColumnIndex(DB.HomeWorkEntry.COLUMN_DATE))
//                homeworks.add(homeworkModel(id, name, note ,date))
                homeworks.add(homeworkModel(id, name, note ))

                cursor.moveToNext()
            }
        }
        return homeworks
    }

    fun readAllHomeworks(): ArrayList<homeworkModel> {
        val homeworks = ArrayList<homeworkModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DB.HomeWorkEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: String
        var name: String
        var note: String
        var date: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getString(cursor.getColumnIndex(DB.HomeWorkEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DB.HomeWorkEntry.COLUMN_NAME))
                note = cursor.getString(cursor.getColumnIndex(DB.HomeWorkEntry.COLUMN_NOTE))
//                date = cursor.getString(cursor.getColumnIndex(DB.HomeWorkEntry.COLUMN_DATE))
//                homeworks.add(homeworkModel(id, name, note,date))
                homeworks.add(homeworkModel(id, name, note))
                cursor.moveToNext()
            }
        }
        return homeworks
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "FeedReader.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DB.HomeWorkEntry.TABLE_NAME + " (" +
                        DB.HomeWorkEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                        DB.HomeWorkEntry.COLUMN_NAME + " TEXT," +
                        DB.HomeWorkEntry.COLUMN_NOTE + " TEXT)"
//                        DB.HomeWorkEntry.COLUMN_DATE + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DB.HomeWorkEntry.TABLE_NAME
    }

}