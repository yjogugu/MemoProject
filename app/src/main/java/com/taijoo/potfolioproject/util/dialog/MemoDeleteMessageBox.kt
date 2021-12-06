package com.taijoo.potfolioproject.util.dialog


import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.CustomDialogMemoLayoutBinding
import com.taijoo.potfolioproject.presentation.view.MainActivity


class MemoDeleteMessageBox(private val activity: Activity , var item : ArrayList<Int>) : Dialog(activity){


    private lateinit var binding : CustomDialogMemoLayoutBinding

    lateinit var title_text : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.custom_dialog_memo_layout,null,false)

        binding.apply {
            title_text = String.format(context.resources.getString(R.string.delete_message_title),item.size)
            activity = this@MemoDeleteMessageBox
        }

        setContentView(binding.root)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window!!.attributes = lpWindow
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()

        window!!.setLayout(width,ViewGroup.LayoutParams.WRAP_CONTENT)


        init()

    }

    fun init(){


        //취소
        binding.frmNo.setOnClickListener {
            cancel()
        }

        //삭제
        binding.frmOk.setOnClickListener {
            for (data in item){

                (activity as MainActivity).viewModel.deleteMemoData(activity.adapter.item[data].icon_seq)
            }
            (activity as MainActivity).onDeleteOkClick(1)
            dismiss()
        }
    }

}