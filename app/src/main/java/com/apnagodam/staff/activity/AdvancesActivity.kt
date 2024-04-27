package com.apnagodam.staff.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityAdvancesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvancesActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdvancesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advances)
        setSupportActionBar(binding.tbAdvances)
        setUI()
    }

    private fun setUI(){
        binding.tvAdvanceList.setOnClickListener {
            startActivity(Intent(this,AdvancesListActivity::class.java))
        }
    }
}