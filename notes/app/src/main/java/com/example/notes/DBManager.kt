package com.example.notes

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager {

    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion = 1
    // CREATE TABLE IF NOT EXISTS MyNotes (ID INTEGER PRIMARY KEY, title TEXT, Description TEXT);
    var sqlCreateTable = "CREATE TABLE IF NOT EXISTS "+ dbTable +" ("+ colID +" INTEGER PRIMARY KEY," +
            colTitle +" TEXT, "+colDes +" TEXT);"
    var sqlDB:SQLiteDatabase? = null
    constructor(context: Context){
        var db = DataBaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }
    inner class DataBaseHelperNotes:SQLiteOpenHelper{
        var context:Context? = null
        constructor(context:Context):super(context,dbName,null,dbVersion){
                this.context=context
        }
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"database is created.",Toast.LENGTH_LONG).show()
        }
        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("Drop table IF EXISTS " + dbTable)
        }
    }
    fun Insert(value:ContentValues):Long{
        val ID = sqlDB!!.insert(dbTable,"",value)
        return ID
    }
    fun Query(projection:Array<String>,selection:String,selectionArgs:Array<String>,sororder:String):Cursor{
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB,projection,selection,selectionArgs,null,null,sororder)
        return cursor
    }

    fun Delete(selection: String,selectionArgs: Array<String>): Int {
        val count = sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }
    fun Update(value: ContentValues,selection: String,selectionArgs: Array<String>): Int {
        val count = sqlDB!!.update(dbTable,value,selection,selectionArgs)
        return count
    }
}