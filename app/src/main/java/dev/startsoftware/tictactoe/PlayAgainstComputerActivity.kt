package dev.startsoftware.tictactoe

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class PlayAgainstComputerActivity : AppCompatActivity() {
    private val emptyCells = mutableListOf<TextView>()
    var playerTurn = true
    var gameover = false
    lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainBoard = findViewById<TableLayout>(R.id.mainBoard)
        val btnGoBack= findViewById<Button>(R.id.btn_goback)

        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        for (i in 0 until mainBoard.childCount) {
            val row = mainBoard.getChildAt(i) as TableRow
            for (j in 0 until row.childCount) {
                val cell = row.getChildAt(j) as TextView
                cell.setOnClickListener {
                    processClickEvent(cell)
                }
                emptyCells.add(cell)
            }
        }
    }





    fun processClickEvent(cell: TextView) {
        if (gameover || !playerTurn) {
            vibrate(100)
            return
        }

        val existingValue = cell.text.toString()
        if (existingValue.isNotEmpty()) {
            // Vibrate for errors
            vibrate(100)
            return
        }

        cell.text = "X"
        cell.setTextColor(Color.parseColor("#ff0000"))

        emptyCells.remove(cell)

        val playerWins = checkWin()

        if (playerWins) {
            findViewById<TextView>(R.id.tx_turn).text = "Congrats! You Win!"
            gameover = true
            vibrate(500)
        } else {
            if (emptyCells.isEmpty()) {
                findViewById<TextView>(R.id.tx_turn).text = "It's a draw!"
                gameover = true
                vibrate(500)
            } else {
                findViewById<TextView>(R.id.tx_turn).text = "Computer's Turn"

                Handler().postDelayed({
                    makeComputerMove()
                }, 1000)
            }
        }
    }






    fun makeComputerMove() {
        val mainBoard = findViewById<TableLayout>(R.id.mainBoard)
        emptyCells.clear()

        for (i in 0 until mainBoard.childCount) {
            val row = mainBoard.getChildAt(i) as TableRow
            for (j in 0 until row.childCount) {
                val cell = row.getChildAt(j) as TextView
                if (cell.text.isEmpty()) {
                    emptyCells.add(cell)
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val randomIndex = Random.nextInt(emptyCells.size)
            val cell = emptyCells[randomIndex]
            cell.text = "O"
            cell.setTextColor(Color.parseColor("#00ff00"))

            val computerWins = checkWin()
            if (computerWins) {
                findViewById<TextView>(R.id.tx_turn).text = "Computer Wins!"
                gameover = true
                vibrate(500)
            } else {

                if (!gameover && playerTurn) {
                    findViewById<TextView>(R.id.tx_turn).text = "Your Turn"
                }
            }
        }


        if (emptyCells.isEmpty()) {
            findViewById<TextView>(R.id.tx_turn).text = "It's a draw!"
            gameover = true
            vibrate(500)
        }
    }








    val possible_wins = arrayOf(
        arrayOf(R.id.tx_a1, R.id.tx_a2, R.id.tx_a3),
        arrayOf(R.id.tx_a4, R.id.tx_a5, R.id.tx_a6),
        arrayOf(R.id.tx_a7, R.id.tx_a8, R.id.tx_a9),
        arrayOf(R.id.tx_a1, R.id.tx_a5, R.id.tx_a9),
        arrayOf(R.id.tx_a7, R.id.tx_a5, R.id.tx_a3),
        arrayOf(R.id.tx_a1, R.id.tx_a4, R.id.tx_a7),
        arrayOf(R.id.tx_a2, R.id.tx_a5, R.id.tx_a8),
        arrayOf(R.id.tx_a3, R.id.tx_a6, R.id.tx_a9)
    )

    fun checkWin(): Boolean {
        for (possible in possible_wins) {
            var seqStr = ""
            var playerWon = false
            var computerWon = false
            for (cellId in possible) {
                val cell = findViewById<TextView>(cellId)
                val existingValue = cell.text.toString()
                if (existingValue.isEmpty()) {
                    break
                }
                seqStr += existingValue

                if (seqStr == "OOO") {
                    computerWon = true
                } else if (seqStr == "XXX") {
                    playerWon = true
                }
            }

            if (playerWon) {
                for (cellId in possible) {
                    val cell = findViewById<TextView>(cellId)
                    cell.setBackgroundColor(Color.parseColor("#FF33F3"))
                }
                findViewById<TextView>(R.id.tx_turn).text = "Congrats! You Win!"
                gameover = true
                vibrate(500)
                return true
            } else if (computerWon) {
                for (cellId in possible) {
                    val cell = findViewById<TextView>(cellId)
                    cell.setBackgroundColor(Color.parseColor("#FF33F3"))
                }
                findViewById<TextView>(R.id.tx_turn).text = "Computer Wins!"
                gameover = true
                vibrate(500)
                return true
            }
        }

        if (!gameover && emptyCells.isEmpty()) {
            findViewById<TextView>(R.id.tx_turn).text = "It's a draw!"
            gameover = true
            vibrate(500)
        }

        return false
    }



    private fun vibrate(milliseconds: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }

}
