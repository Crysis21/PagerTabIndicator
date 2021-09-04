package com.hold1.pagertabsdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hold1.pagertabsdemo.databinding.ActivityStartBinding

class StartActivity: AppCompatActivity() {

    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager1.setOnClickListener {
            startActivity(Intent(this, ViewPagerActivity::class.java))
        }

        binding.viewPager2.setOnClickListener {
            startActivity(Intent(this, ViewPager2Activity::class.java))
        }
    }
}