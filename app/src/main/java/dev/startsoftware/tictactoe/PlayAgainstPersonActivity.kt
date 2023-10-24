package dev.startsoftware.tictactoe

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayAgainstPersonActivity : AppCompatActivity() {
    var turn = 1
    var gameover = false
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        var cells = arrayOf(
            R.id.tx_a1, R.id.tx_a2, R.id.tx_a3,
            R.id.tx_a4, R.id.tx_a5, R.id.tx_a6,
            R.id.tx_a7, R.id.tx_a8, R.id.tx_a9
        )

        val btnGoBack = findViewById<Button>(R.id.btn_goback)

        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        for (cell in cells) {
            val item = findViewById<TextView>(cell)
            item.setOnClickListener { processClickEvent(cell) }
        }
    }

    fun processClickEvent(cellId: Int) {
        if (gameover) return
        val existingValue: String = findViewById<TextView>(cellId).text.toString()
        if (existingValue.isNotEmpty()) {
            // Handle an error (vibrate when clicking on a filled square)
            vibrator.vibrate(100) // Vibrate for 100 milliseconds
            return
        }

        if (turn == 1) {
            findViewById<TextView>(cellId).text = "X"
            findViewById<TextView>(cellId).setTextColor(Color.parseColor("#ff0000"))
        } else {
            findViewById<TextView>(cellId).text = "O"
            findViewById<TextView>(cellId).setTextColor(Color.parseColor("#00ff00"))
        }

        val win = checkWin()
        if (win) {
            findViewById<TextView>(R.id.tx_turn).text = "Congrats to Player " + turn
            gameover = true
            vibrator.vibrate(500)
            return
        }

        turn = if (turn == 1) 2 else 1
        findViewById<TextView>(R.id.tx_turn).text = "Turn of Player: " + turn

        if (isDraw()) {
            findViewById<TextView>(R.id.tx_turn).text = "It's a draw!"
            gameover = true
            vibrator.vibrate(500)
        }
    }


    var possible_wins = arrayOf(
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
            for (cellId in possible) {
                val existingValue: String = findViewById<TextView>(cellId).text.toString()
                if (existingValue.isEmpty()) break
                seqStr = seqStr + existingValue
            }
            if (seqStr == "OOO" || seqStr == "XXX") {
                for (cellId in possible) {
                    findViewById<TextView>(cellId).setBackgroundColor(Color.parseColor("#FF33F3"))
                }
                return true
            }
        }
        return false
    }

    fun isDraw(): Boolean {
        for (cellId in arrayOf(
            R.id.tx_a1, R.id.tx_a2, R.id.tx_a3,
            R.id.tx_a4, R.id.tx_a5, R.id.tx_a6,
            R.id.tx_a7, R.id.tx_a8, R.id.tx_a9
        )) {
            val existingValue: String = findViewById<TextView>(cellId).text.toString()
            if (existingValue.isEmpty()) return false
        }
        return true
    }
}
