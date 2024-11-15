package org.antani.sedga

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private var isSedgaActive = false
    private var startTime: Long = 0
    private lateinit var database: SedgaDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = SedgaDatabase.getDatabase(this)

        findViewById<Button>(R.id.checklistButton).setOnClickListener {
            startActivity(Intent(this, ChecklistActivity::class.java))
        }

        findViewById<Button>(R.id.sedgaButton).setOnClickListener {
            toggleSedga()
        }

        findViewById<Button>(R.id.webButton).setOnClickListener {
            val url = "https://pornhub.com"
            val intent = Intent("android.support.customtabs.action.ACTION_START_IN_INCOGNITO_MODE")
            intent.setPackage("com.android.chrome")
            intent.data = Uri.parse(url)

            try {
                startActivity(intent)
            } catch (e: Exception) {
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(fallbackIntent)
            }
        }

        findViewById<Button>(R.id.historyButton).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        // Aggiungi qui il nuovo click listener per gli achievement
        findViewById<Button>(R.id.achievementsButton).setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }
    }


    private fun toggleSedga() {
        if (!isSedgaActive) {
            startSedga()
        } else {
            endSedga()
        }
    }

    private fun startSedga() {
        TelegramService.sendMessage { success ->
            runOnUiThread {
                if (success) {
                    isSedgaActive = true
                    startTime = System.currentTimeMillis()
                    findViewById<Button>(R.id.sedgaButton).text = "Termina sedga"
                    Toast.makeText(this, "Sedga annunciata", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Errore nell'invio del messaggio", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun endSedga() {
        val duration = System.currentTimeMillis() - startTime
        val minutes = duration / 1000 / 60
        val seconds = (duration / 1000) % 60

        isSedgaActive = false
        findViewById<Button>(R.id.sedgaButton).text = "Inizia sedga"
        Toast.makeText(this, "Durata sedga: ${minutes}m ${seconds}s", Toast.LENGTH_LONG).show()

        // Salva la sedga nel database e controlla gli achievement
        lifecycleScope.launch {
            val sedga = SedgaEntity(
                timestamp = startTime,
                duration = duration
            )
            database.sedgaDao().insertSedga(sedga)

            // Controlla gli achievement
            checkAchievements()
        }
    }

    private suspend fun checkAchievements() {
        val dao = database.sedgaDao()

        // Controlla il totale delle sedghe
        val totalSedghe = dao.getTotalSedghe()
        if (totalSedghe == 1) {
            showAchievement("Prima Volta")
        }

        // Controlla sedghe veloci
        if (dao.getSpeedSedghe() > 0) {
            showAchievement("Velocista")
        }

        // Controlla sedghe lunghe
        if (dao.getMarathonSedghe() > 0) {
            showAchievement("Maratoneta")
        }

        // Controlla sedghe giornaliere
        val today = Date().toString()
        if (dao.getSedgheCountForDay(today) >= 3) {
            showAchievement("Routine Quotidiana")
        }

        // Controlla orario
        val calendar = Calendar.getInstance()
        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..4 -> showAchievement("Gufo Notturno")
            in 5..7 -> showAchievement("Mattiniero")
        }
    }

    private fun showAchievement(title: String) {
        runOnUiThread {
            Toast.makeText(
                this,
                "🏆 Achievement Sbloccato: $title",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}