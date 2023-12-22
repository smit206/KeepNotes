package com.example.notes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class activity_noteinfo : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noteinfo)

        val nam = findViewById<TextView>(R.id.opTitle)
        val dess = findViewById<TextView>(R.id.opDes)

        val bundle:Bundle = intent.extras!!
        val name = bundle.getString("name")
        val des = bundle.getString("des")

        nam.text = name
        dess.text = des
    }
}