package org.antani.sedga

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ChecklistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }
    }
}