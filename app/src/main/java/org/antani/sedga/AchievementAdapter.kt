package org.antani.sedga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AchievementAdapter(private var achievements: List<Achievement>) :
    RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.achievementIcon)
        val title: TextView = view.findViewById(R.id.achievementTitle)
        val description: TextView = view.findViewById(R.id.achievementDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val achievement = achievements[position]
        holder.title.text = achievement.title
        holder.description.text = achievement.description

        // Cambia l'opacità se l'achievement non è sbloccato
        holder.itemView.alpha = if (achievement.isUnlocked) 1.0f else 0.5f
    }

    override fun getItemCount() = achievements.size

    fun updateAchievements(newAchievements: List<Achievement>) {
        achievements = newAchievements
        notifyDataSetChanged()
    }
}