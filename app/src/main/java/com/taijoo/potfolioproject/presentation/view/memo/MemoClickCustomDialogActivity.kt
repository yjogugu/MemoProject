package com.taijoo.potfolioproject.presentation.view.memo

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.MyApplication
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.databinding.ActivityMemoClickCustomDialogAcitivtyBinding
import com.taijoo.potfolioproject.presentation.view.memo.memoAdd.MemoAddActivity
import com.taijoo.potfolioproject.presentation.view.calendar.CalendarDialog

class MemoClickCustomDialogActivity : AppCompatActivity() {

    lateinit var binding : ActivityMemoClickCustomDialogAcitivtyBinding
    lateinit var viewmodel : MemoViewModel

    var icon_position = 0

    lateinit var memoItem : Memo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_memo_click_custom_dialog_acitivty)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_memo_click_custom_dialog_acitivty
        )

        viewmodel = MyApplication.instance.mainActivity.viewModel


        binding.apply {
            icon_position = intent.getIntExtra("icon_position",0)
            memoItem = viewmodel.memoData.value!![icon_position]

            //배경색이 흰색이면 텍스트색상 + close  아이콘 색상 검은색으로 변경
            if(memoItem.icon_color_position == 13){
                ivClose.imageTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                ivEdit.imageTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                memoText.setTextColor(Color.parseColor("#000000"))
                memoTitleText.setTextColor(Color.parseColor("#000000"))
                memoDate.setTextColor(Color.parseColor("#000000"))
            }
            else{
                ivClose.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                ivEdit.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                memoText.setTextColor(Color.parseColor("#FFFFFF"))
                memoTitleText.setTextColor(Color.parseColor("#FFFFFF"))
                memoDate.setTextColor(Color.parseColor("#FFFFFF"))
            }

            memo = memoItem

            activity = this@MemoClickCustomDialogActivity
            lifecycleOwner = this@MemoClickCustomDialogActivity

        }

        init()
    }

    fun init(){

        viewmodel.memoData.observe(this,{
            if(viewmodel.getDataType() == 3){
                memoItem = it[icon_position]
                binding.memo = memoItem
                viewmodel.setDataType(-1)
            }

        })
    }

    fun onEditClick(){
        val intent  = Intent(this , MemoAddActivity::class.java)
        intent.putExtra("activity_type",1)
        intent.putExtra("icon_position",icon_position)
        intent.putExtra("icon_seq", memoItem.icon_seq.toInt())
        intent.putExtra("icon_color_position", memoItem.icon_color_position)
        intent.putExtra("title", memoItem.memo_title)
        intent.putExtra("content", memoItem.memo_content)
        startActivity(intent)
    }

    fun onCalendar(){
        CalendarDialog(2,memoItem).show(supportFragmentManager,"CalendarDialog")
    }

    override fun onBackPressed() {
        binding.memoText.visibility = View.GONE
        binding.ivClose.visibility = View.GONE
        binding.ivEdit.visibility = View.GONE
        super.onBackPressed()
    }
}