package com.example.notes

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class   AddNotes : AppCompatActivity() {

    var edittitle:EditText? = null
    var editdes:EditText? = null
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        edittitle = findViewById<EditText>(R.id.etTitle)
        editdes = findViewById<EditText>(R.id.etDes)

        try {
            var bundle:Bundle= intent.extras!!
            id = bundle.getInt("ID",0)
            if (id != 0) {
                edittitle!!.setText(bundle.getString("name").toString())
                editdes!!.setText(bundle.getString("des").toString())
            }
        }catch (ex:Exception){
            ex.message
        }

    }

    fun buAdd(view: View){
        var dbManager = DBManager(this@AddNotes)
        var values = ContentValues()
        values.put("Title",edittitle!!.text.toString())
        values.put("Description",editdes!!.text.toString())

        if(id == 0) {
            val ID = dbManager.Insert(values)
            if (ID > 0) {
                Toast.makeText(this, " note is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, " cannot add note", Toast.LENGTH_LONG).show()
            }
        }
        else{
            var selectionArs = arrayOf(id.toString())
            val ID = dbManager.Update(values,"ID=?",selectionArs)
            if (ID > 0) {
                Toast.makeText(this, " note is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, " cannot add note", Toast.LENGTH_LONG).show()
            }
        }
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}