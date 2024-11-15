package org.antani.sedga

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HistoryActivity : AppCompatActivity() {
    private lateinit var adapter: SedgaAdapter
    private lateinit var database: SedgaDatabase
    private lateinit var chart: LineChart
    private var totalSedghe = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        database = SedgaDatabase.getDatabase(this)
        chart = findViewById(R.id.chart)
        setupChart()

        val recyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SedgaAdapter(emptyList())
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }

        loadData()
    }

    private fun setupChart() {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawGridBackground(false)

            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
            legend.isEnabled = false

            setBackgroundColor(android.graphics.Color.TRANSPARENT)

            setExtraOffsets(10f, 10f, 10f, 20f)

            xAxis.apply {
                textColor = android.graphics.Color.parseColor("#9B6BFF")
                textSize = 14f
                position = XAxis.XAxisPosition.BOTTOM
                setDrawAxisLine(false)
                granularity = 1f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        // Aggiungiamo 1 perché value parte da 0
                        return (value + 1).toInt().toString()
                    }
                }
                yOffset = 10f
            }

            axisLeft.apply {
                textColor = android.graphics.Color.parseColor("#9B6BFF")
                textSize = 14f
                setDrawAxisLine(false)
                gridColor = android.graphics.Color.parseColor("#22FFFFFF")
                granularity = 0.5f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val totalSeconds = (value * 60).toInt()
                        val minutes = totalSeconds / 60
                        val seconds = totalSeconds % 60
                        return if (seconds == 0) {
                            "${minutes}m"
                        } else {
                            "${minutes}m ${seconds}s"
                        }
                    }
                }
            }

            animateX(1000)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            val dao = database.sedgaDao()

            val sedghe = dao.getAllSedghe()
            totalSedghe = sedghe.size
            adapter.updateSedghe(sedghe)

            val totalCount = dao.getTotalSedghe()
            findViewById<TextView>(R.id.totalSedgheCount).text = totalCount.toString()

            val avgDuration = dao.getAverageDuration()
            val avgMinutes = (avgDuration / 1000.0 / 60.0).toInt()
            findViewById<TextView>(R.id.averageDuration).text = "${avgMinutes}m"

            // Non invertiamo più l'ordine delle sedghe
            val entries = sedghe.mapIndexed { index, sedga ->
                val minutes = sedga.duration / 1000.0 / 60.0
                Entry(index.toFloat(), minutes.toFloat())
            }

            val dataSet = LineDataSet(entries, "Durata Sedghe").apply {
                color = android.graphics.Color.parseColor("#9B6BFF")
                setCircleColor(android.graphics.Color.parseColor("#9B6BFF"))
                lineWidth = 3f
                circleRadius = 5f
                setDrawFilled(true)
                fillColor = android.graphics.Color.parseColor("#4D9B6BFF")
                fillAlpha = 50
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.2f

                setDrawCircleHole(true)
                circleHoleColor = android.graphics.Color.parseColor("#9B6BFF")
                circleHoleRadius = 2.5f
            }

            chart.data = LineData(dataSet)
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    }
}