package com.hal.convenientfile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hal.convenientfile.R

class JumpFActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jump_factivity)
        finish()
    }
}