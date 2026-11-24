package com.example.gallerytile

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    private lateinit var prefs: SharedPreferences

    private val picker = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            prefs.edit().putString("avatar_uri", uri.toString()).apply()
            openAvatar(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("gallery_tile", MODE_PRIVATE)

        val hint = TextView(this).apply {
            text = "1. Нажми «Выбрать аватар» и выбери фото из галереи.\n\n" +
                    "2. Откроется полноэкранный показ этого фото.\n\n" +
                    "3. В своём приложении для стрима включи «Демонстрация экрана» вместо камеры, пока открыт этот экран."
            textSize = 16f
            setPadding(48, 96, 48, 48)
        }

        val chooseBtn = Button(this).apply {
            text = "Выбрать аватар"
            setOnClickListener {
                picker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }

        val showBtn = Button(this).apply {
            text = "Показать сохранённый аватар"
            setOnClickListener {
                val saved = prefs.getString("avatar_uri", null)
                if (saved != null) openAvatar(Uri.parse(saved))
            }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            addView(hint)
            addView(chooseBtn)
            addView(showBtn)
        }
        setContentView(layout)
    }

    private fun openAvatar(uri: Uri) {
        val intent = Intent(this, AvatarActivity::class.java)
        intent.data = uri
        startActivity(intent)
    }
}
