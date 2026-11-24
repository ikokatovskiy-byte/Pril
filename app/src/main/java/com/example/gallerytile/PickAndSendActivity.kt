package com.example.gallerytile

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class PickAndSendActivity : ComponentActivity() {

    companion object {
        // Пусто = системное меню "Отправить в...". Позже сюда можно вписать пакет своего приложения.
        const val TARGET_PACKAGE = ""
    }

    private val picker = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) send(uri) else finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            picker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun send(uri: Uri) {
        val share = Intent(Intent.ACTION_SEND).apply {
            type = contentResolver.getType(uri) ?: "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newRawUri("image", uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try {
            if (TARGET_PACKAGE.isNotEmpty()) {
                share.setPackage(TARGET_PACKAGE)
                startActivity(share)
            } else {
                startActivity(Intent.createChooser(share, "Отправить в…"))
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Нет приложения, принимающего изображения", Toast.LENGTH_LONG).show()
        }
        finish()
    }
}
