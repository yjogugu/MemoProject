package com.taijoo.potfolioproject.util.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.ItemLayoutColorRecyclerBinding
import com.taijoo.potfolioproject.util.InterFace.ColorDialogInterFace
import com.taijoo.potfolioproject.util.ColorDialogItem
import com.taijoo.potfolioproject.util.Color_Code

class ColorDialog(context: Context) : Dialog(context){

    lateinit var colorAdapter: ColorAdapter//백그라운드 컬러 어뎁터

    var colorDialogItem:ArrayList<ColorDialogItem> = ArrayList()//컬러 어레이
    val colorCode:Color_Code = Color_Code()//컬러 코드

    lateinit var background_layoutManager : RecyclerView.LayoutManager

    lateinit var colorDialogInterFace : ColorDialogInterFace //인터페이스 MemoAddActivity 사용

    lateinit var binding : ItemLayoutColorRecyclerBinding

    var color_position = 0//컬러 포지션 MemoAddActivity 에서 부터 가져와서 유지

    fun setInterface(colorDialogInterFace : ColorDialogInterFace, color_position : Int){
        this.colorDialogInterFace = colorDialogInterFace
        this.color_position = color_position
        show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window!!.attributes = lpWindow


        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_layout_color_recycler,null,false)

        setContentView(binding.root)

        init()

        onClick()
    }

    fun init(){
        //컬러 셋팅
        colorDialogItem = colorCode.setColor()

        //리사이클러뷰 셋팅
        if(binding.backgroundColorRecyclerview.adapter == null){
            binding.backgroundColorRecyclerview.setHasFixedSize(true)//배경사진 리사이클러뷰 셋팅
            background_layoutManager = LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL , false)
            binding.backgroundColorRecyclerview.layoutManager = background_layoutManager
            colorAdapter = ColorAdapter(context,color_position,colorDialogItem,colorDialogInterFace)

            binding.backgroundColorRecyclerview.adapter = colorAdapter
        }

    }

    fun onClick(){
        binding.okTextview.setOnClickListener {
            dismiss()
        }
    }

}