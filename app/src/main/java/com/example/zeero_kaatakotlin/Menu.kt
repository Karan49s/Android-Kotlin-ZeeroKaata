package com.example.zeero_kaatakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class Menu : AppCompatActivity() {

    lateinit var single : Button
    lateinit var  multi : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        single = findViewById(R.id.btnSingle)
        multi = findViewById(R.id.btnMulti)

        val intent = Intent(this@Menu, MainActivity::class.java)

        single.setOnClickListener() {
            intent.putExtra("mode",1)
            startActivity( intent )
        }

        multi.setOnClickListener() {
            intent.putExtra("mode",0)
            startActivity( intent )
        }


    }
}
