package com.example.gallerytile

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = "Потяни шторку вниз, нажми «Изменить» и перетащи плитку «Фото → отправить» в активные."
            textSize = 18f
            setPadding(48, 96, 48, 48)
        })
    }
}
