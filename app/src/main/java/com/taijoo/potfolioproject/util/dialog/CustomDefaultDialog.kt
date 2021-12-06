package com.taijoo.potfolioproject.util.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.CustomDefaultDialogLayoutBinding

class CustomDefaultDialog (val activity: Context , val title : String , val content : String)  : Dialog(activity){


    private lateinit var binding : CustomDefaultDialogLayoutBinding

    private lateinit var customDialogListener : CustomDialogListener


    interface CustomDialogListener{
        fun onCheckClick()

        fun onNoClick()
    }

    fun setDialogListener(customDialogListener : CustomDialogListener){
        this.customDialogListener = customDialogListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.custom_default_dialog_layout,null,false)

        binding.apply {
            activity = this@CustomDefaultDialog
        }

        setContentView(binding.root)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window!!.attributes = lpWindow
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()

        window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)


        binding.tvOk.setOnClickListener {
            customDialogListener.onCheckClick()
            dismiss()
        }

        binding.tvNo.setOnClickListener {
            customDialogListener.onNoClick()
            cancel()
        }

    }

}