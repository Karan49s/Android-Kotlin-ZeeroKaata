package com.example.zeero_kaatakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.properties.Delegates

class GameResultActivity : AppCompatActivity() {

    lateinit var img : ImageView
    lateinit var text: TextView
    lateinit var playAgain : Button
    var mode =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)
        img=findViewById(R.id.imgResult)
        text = findViewById(R.id.greetingText)
        playAgain = findViewById(R.id.playAgain)

        mode = intent.getIntExtra("mode",0)
        val Winner =intent!!.getIntExtra("winner",0)

        if(mode==0) {
            if(Winner==1) {
                img.setImageResource(R.drawable.win)
                text.setText("X WINS")

            }else if(Winner==2){
                img.setImageResource(R.drawable.win)
                text.setText("O WINS")
            }else{
                img.setImageResource(R.drawable.draw)
                text.setText("DRAW :(")
            }
        }else{
            if(Winner==1){
                img.setImageResource(R.drawable.win)
                text.setText("Congratulations You WIN")
            }else if(Winner ==2){
                img.setImageResource(R.drawable.lose)
                text.setText("You LOSE !!")
            }else{
                img.setImageResource(R.drawable.draw)
                text.setText("DRAW !!!")
            }
        }

        playAgain.setOnClickListener {
            val intent = Intent(this@GameResultActivity, MainActivity::class.java)
            intent.putExtra("mode",mode)
            startActivity( intent )
            finish()
        }


    }
    override fun onBackPressed(){
        val intent = Intent(this@GameResultActivity, MainActivity::class.java)
        intent.putExtra("mode",mode)
        startActivity( intent )
        finish()
    }
}
