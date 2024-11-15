package org.antani.sedga

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val iconResource: Int? = null
) {
    companion object {
        val ACHIEVEMENTS = listOf(
            Achievement(
                "first_sedga",
                "Prima Volta",
                "Hai completato la tua prima sedga!"
            ),
            Achievement(
                "speed_demon",
                "Velocista",
                "Sedga completata in meno di 5 minuti"
            ),
            Achievement(
                "marathon",
                "Maratoneta",
                "Sedga di oltre 30 minuti"
            ),
            Achievement(
                "daily_routine",
                "Routine Quotidiana",
                "3 sedghe in un giorno"
            ),
            Achievement(
                "night_owl",
                "Gufo Notturno",
                "Sedga dopo mezzanotte"
            ),
            Achievement(
                "early_bird",
                "Mattiniero",
                "Sedga prima delle 7:00"
            ),
            Achievement(
                "dedication",
                "Dedizione",
                "30 sedghe in un mese"
            )
        )
    }
}