package com.jdt.waltrackv2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.ActivityAddWalletBinding

class AddWalletActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddWalletBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionbar = supportActionBar
        actionbar?.title = "Add Wallet"
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowTitleEnabled(true)

        val navigationIcon = binding.toolbar.navigationIcon

        navigationIcon?.let {
            val color = ContextCompat.getColor(this, R.color.white)
            DrawableCompat.setTint(it, color)
            binding.toolbar.navigationIcon = it
        }
        binding.toolbar.setNavigationOnClickListener{
            finish()
        }
    }
}