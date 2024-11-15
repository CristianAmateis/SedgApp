package org.antani.sedga

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class AchievementsActivity : AppCompatActivity() {
    private lateinit var adapter: AchievementAdapter
    private lateinit var database: SedgaDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        database = SedgaDatabase.getDatabase(this)

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.achievementsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AchievementAdapter(emptyList())
        recyclerView.adapter = adapter

        // Carica gli achievement
        loadAchievements()

        // Pulsante indietro
        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun loadAchievements() {
        lifecycleScope.launch {
            val dao = database.sedgaDao()
            val totalSedghe = dao.getTotalSedghe()
            val speedSedghe = dao.getSpeedSedghe()
            val marathonSedghe = dao.getMarathonSedghe()
            val today = java.util.Date().toString()
            val dailySedghe = dao.getSedgheCountForDay(today)

            val achievements = Achievement.ACHIEVEMENTS.map { achievement ->
                achievement.copy(isUnlocked = when (achievement.id) {
                    "first_sedga" -> totalSedghe >= 1
                    "speed_demon" -> speedSedghe > 0
                    "marathon" -> marathonSedghe > 0
                    "daily_routine" -> dailySedghe >= 3
                    "night_owl" -> isNightTime()
                    "early_bird" -> isEarlyMorning()
                    "dedication" -> totalSedghe >= 30
                    else -> false
                })
            }

            adapter.updateAchievements(achievements)
        }
    }

    private fun isNightTime(): Boolean {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return hour in 0..4
    }

    private fun isEarlyMorning(): Boolean {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return hour in 5..7
    }
}