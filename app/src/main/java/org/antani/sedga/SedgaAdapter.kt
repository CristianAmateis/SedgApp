package org.antani.sedga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class SedgaAdapter(private var sedghe: List<SedgaEntity>) :
    RecyclerView.Adapter<SedgaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.dateText)
        val durationText: TextView = view.findViewById(R.id.durationText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sedga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sedga = sedghe[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = Date(sedga.timestamp)
        holder.dateText.text = dateFormat.format(date)

        val minutes = sedga.duration / 1000 / 60
        val seconds = (sedga.duration / 1000) % 60
        holder.durationText.text = "${minutes}m ${seconds}s"
    }

    override fun getItemCount() = sedghe.size

    fun updateSedghe(newSedghe: List<SedgaEntity>) {
        sedghe = newSedghe
        notifyDataSetChanged()
    }
}