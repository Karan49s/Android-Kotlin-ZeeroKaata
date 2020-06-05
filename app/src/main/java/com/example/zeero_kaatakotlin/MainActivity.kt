package com.example.zeero_kaatakotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {


    /**decleration and initilisation of diffrent variables we are gonna use throughout the code */
    lateinit var movesHistory : TextView
    var mode by Delegates.notNull<Int>()
    lateinit var restart : ImageView
    val posId = IntArray(10) { i ->R.id.pos1 + i -1}
    var board = IntArray(10){-1}
    var token = R.drawable.token_x
    var turn = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mode = intent.getIntExtra("mode", 0)

        movesHistory = findViewById(R.id.movesHistory)
        restart= findViewById(R.id.restart)

        /** The lambda implementation of a on click listner*/
        restart.setOnClickListener{
            reset()
        }

        reset()

    }


    /** Reset function to reset the board, moves and clickable behaviour also set image drawable to null*/
    fun reset(){
        board=IntArray(10){-1}
        board[0]=0
        token= R.drawable.token_x
        pos1.isClickable=true ; pos1.setImageDrawable(null)
        pos2.isClickable=true ; pos2.setImageDrawable(null)
        pos3.isClickable=true ; pos3.setImageDrawable(null)
        pos4.isClickable=true ; pos4.setImageDrawable(null)
        pos5.isClickable=true ; pos5.setImageDrawable(null)
        pos6.isClickable=true ; pos6.setImageDrawable(null)
        pos7.isClickable=true ; pos7.setImageDrawable(null)
        pos8.isClickable=true ; pos8.setImageDrawable(null)
        pos9.isClickable=true ; pos9.setImageDrawable(null)
    }

    /**play function is invoked everytime the user touches any imageView and sets the view to the token*/
    fun play(view: View) {
        val position = findViewById<ImageView>(view.id)
        position.setImageResource(token)
        position.isClickable = false
        board[(1+ view.id - R.id.pos1)]=turn   //converting the position of view from id to int bw 1 to 9

        /**move history stores the positions taken in the last move and show it in a scroll view below the game board
         * so that even if
         * someone is not paying attention he/she may not get confused during the game that who took which position*/

        /** STRING TEMPLATES ARE USED HERE*/
        movesHistory.text = "\n\n${if (turn == 1) { "X" } else { "O" }} took  position ${1 + view.id - R.id.pos1} \n\n\n\n${movesHistory.text}"

        board[0]++   /** Using the 0th position to store the no. of moves*/
        changeMove()
        //checking if the player made a winning move everytime player makes a move else checking if it is a draw
        if(isWinner(board)==1){
            //calling the winner function after 1 second delay so that player can see what was the last move that was made by the opponent
            Handler().postDelayed({Winner(1)},1000)
        }else{
            //calling the isDraw function after 1 second delay so that player can see what was the last move that was made by the opponent
            Handler().postDelayed({isDraw()},1000)

        }
        /** if mode is multiplayer then call function cpuMove corresponding to every move the user makes else do nothing*/
        if(mode==1 && turn ==2)
        {
            cpuMove()
            //checking if the cpu made a winning move everytime cpu makes a move else checking if it is a draw
            if(isWinner(board)==2){
                //calling the winner function after 1 second delay so that player can see what was the last move that was made by the opponent
                Handler().postDelayed({Winner(2)},1000)
            }else{
                //calling the isDraw function after 1 second delay so that player can see what was the last move that was made by the opponent
                Handler().postDelayed({isDraw()},1000)
            }
            board[0]++   /** Using the 0th position to store the no. of moves*/
            changeMove()
        }
    }

    /**this function changes the image and turn variable to be set by anywhere in the code based on whose turn it is*/
    fun changeMove(){
        if(board[0]%2==0) {
            token = R.drawable.token_x
            turn = 1
        }else{
            token = R.drawable.token_o
            turn =2
        }

    }

    /**This cpu Move function is called if the mode of the game is 1 ie local multiplayer */
    fun cpuMove() {
        /**this for loop to check if occupying any empty position make the cpu win the game if yes then take the position and win*/
        for(i in 1..9) {
            if(board[i]==-1){
                board[i]=2
                if(isWinner(board)==2) {
                    movesHistory.text = "\n\nO  took  position ${i} \n\n\n\n${movesHistory.text}"
                    val position = findViewById<ImageView>(posId[i])
                    position.setImageResource(token)
                    position.isClickable = false
                    board[i]=2
                    return
                }else{
                    board[i]=-1
                }
            }
        }
        /**this for loop to check if occupying any empty position make the player win the game
         * if yes then take the position and stop the player from winning*/
        for(i in 1..9) {
            if(board[i]==-1){
                board[i]=1
                if(isWinner(board)==1) {
                    movesHistory.text = "\n\nO  took  position ${i} \n\n\n\n${movesHistory.text}"
                    val position = findViewById<ImageView>(posId[i])
                    position.setImageResource(token)
                    position.isClickable = false
                    board[i]=2
                    return
                }else{
                    board[i]=-1
                }
            }
        }
        /** if nothing of any above loops works i.e,
         * neither the cpu wins nor the player in a single move
         * then take a random position but here
         * not completely random this corPre stands for corner prefrence
         * it will preferably take a corner then the center if empty
         * at last it will take the edges if none of them is occupied*/
        val corPre = arrayOf(1,3,7,9,5,2,4,6,8)
        for(i in corPre){
            if(board[i]==-1){
                movesHistory.text = "\n\nO  took  position ${i} \n\n\n\n${movesHistory.text}"
                val position = findViewById<ImageView>(posId[i])
                position.setImageResource(token)
                position.isClickable = false
                board[i]=2
                return
            }
        }
    }

    /**  isDraw function to check if the game is a draw and go to next page*/
    fun isDraw(){
        if(board[0]==9)
        {
            val intent = Intent(this@MainActivity, GameResultActivity::class.java)
            intent.putExtra("mode",mode)
            startActivity( intent )
            finish()
        }

    }

    /** Winner function which is called if there is a winner and pass the mode and winner to next page */
    fun Winner(int: Int){
        val intent = Intent(this@MainActivity, GameResultActivity::class.java)
        intent.putExtra("mode",mode)
        intent.putExtra("winner",int)
        startActivity( intent )
        finish() // finish the activity once we left the page
    }

    /** isWinner Function to check if there is a winner at the current state of the board*/
    fun isWinner( tempboard: IntArray ) : Int{
        if( ( tempboard[1]==1 && tempboard[2]==1 && tempboard[3]==1 ) ||
            ( tempboard[4]==1 && tempboard[5]==1 && tempboard[6]==1 ) ||
            ( tempboard[7]==1 && tempboard[8]==1 && tempboard[9]==1 ) ||
            ( tempboard[1]==1 && tempboard[4]==1 && tempboard[7]==1 ) ||
            ( tempboard[2]==1 && tempboard[5]==1 && tempboard[8]==1 ) ||
            ( tempboard[3]==1 && tempboard[6]==1 && tempboard[9]==1 ) ||
            ( tempboard[1]==1 && tempboard[5]==1 && tempboard[9]==1 ) ||
            ( tempboard[3]==1 && tempboard[5]==1 && tempboard[7]==1 ) )
        {
            pos1.isClickable=false // If there is a winner then dont let the user click any more buttons
            pos2.isClickable=false
            pos3.isClickable=false
            pos4.isClickable=false
            pos5.isClickable=false
            pos6.isClickable=false
            pos7.isClickable=false
            pos8.isClickable=false
            pos9.isClickable=false
            return 1  //Returns 1 if X is a winner at the current state of the board
        }
        else if(( tempboard[1]==2 && tempboard[2]==2 && tempboard[3]==2 ) ||
            ( tempboard[4]==2 && tempboard[5]==2 && tempboard[6]==2 ) ||
            ( tempboard[7]==2 && tempboard[8]==2 && tempboard[9]==2 ) ||
            ( tempboard[1]==2 && tempboard[4]==2 && tempboard[7]==2 ) ||
            ( tempboard[2]==2 && tempboard[5]==2 && tempboard[8]==2 ) ||
            ( tempboard[3]==2 && tempboard[6]==2 && tempboard[9]==2 ) ||
            ( tempboard[1]==2 && tempboard[5]==2 && tempboard[9]==2 ) ||
            ( tempboard[3]==2 && tempboard[5]==2 && tempboard[7]==2 ) )
        {
            pos1.isClickable=false  // If there is a winner then dont let the user click any more buttons
            pos2.isClickable=false
            pos3.isClickable=false
            pos4.isClickable=false
            pos5.isClickable=false
            pos6.isClickable=false
            pos7.isClickable=false
            pos8.isClickable=false
            pos9.isClickable=false
            return 2   // Returns 2 if X is a winner at the current state of the board
        }
        else {
            return 0  // else returns 0
        }
    }




}
