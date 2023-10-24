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
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        val btnPlayAgainstComputer = findViewById<Button>(R.id.btnPlayAgainstComputer)
        val btnPlayAgainstPerson = findViewById<Button>(R.id.btnPlayAgainstPerson)

        btnPlayAgainstComputer.setOnClickListener {
            val intent = Intent(this, PlayAgainstComputerActivity::class.java)
            startActivity(intent)
        }

        btnPlayAgainstPerson.setOnClickListener {
            val intent = Intent(this, PlayAgainstPersonActivity::class.java)
            startActivity(intent)
        }
    }
}
