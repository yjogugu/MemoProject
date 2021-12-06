package com.taijoo.potfolioproject.presentation.view.opensource

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.ActivityOpenSourceBinding

class OpenSourceActivity : AppCompatActivity() {

    lateinit var binding : ActivityOpenSourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_open_source)

        binding = DataBindingUtil.setContentView(this , R.layout.activity_open_source)

        binding.apply {
            lifecycleOwner = this@OpenSourceActivity
        }

        init()
    }

    fun init(){

        //뒤로가기
        binding.titleAppbar.ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}