package com.taijoo.potfolioproject.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.ActivityZoomInBinding

class ZoomInActivity : AppCompatActivity() {
    lateinit var uri : String
    lateinit var binding : ActivityZoomInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_zoom_in)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_zoom_in)

        uri = intent.getStringExtra("uri")!!

        binding.apply {
            activity = this@ZoomInActivity
            lifecycleOwner = this@ZoomInActivity
        }

    }

    fun init(){

    }
}