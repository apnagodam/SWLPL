package com.apnagodam.staff.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apnagodam.staff.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvancesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advances_list)
    }
}