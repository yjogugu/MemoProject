package com.taijoo.potfolioproject.presentation.view.memo.memoAdd

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.MyApplication
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.ActivityMemoAddBinding
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModel
import com.taijoo.potfolioproject.util.ColorDialogItem
import com.taijoo.potfolioproject.util.Color_Code
import com.taijoo.potfolioproject.util.InterFace.ColorDialogInterFace
import com.taijoo.potfolioproject.util.dialog.ColorDialog

//메모 추가 View
class MemoAddActivity : AppCompatActivity(), ColorDialogInterFace  {

    lateinit var binding : ActivityMemoAddBinding
    lateinit var viewmodel : MemoViewModel

    lateinit var colorDialogInterFace : ColorDialogInterFace

    var icon_position = 0
    var icon_color_position = 0
    var activity_type = 0//0작성 , 1수정

    var colorDialogItem:ArrayList<ColorDialogItem> = ArrayList()//컬러 어레이
    val colorCode: Color_Code = Color_Code()//컬러 코드

    var icon_seq = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_memo_add)

        viewmodel = MyApplication.instance.mainActivity.viewModel

        colorDialogItem = colorCode.setColor()

        binding.apply {
            activity_type = intent.getIntExtra("activity_type",0)

            if(activity_type == 0){//메모 생성
                color = Color.parseColor("#555555")
                tilMemoInput.hintTextColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            }
            else if(activity_type == 1){//메모 수정
                icon_color_position = intent.getIntExtra("icon_color_position",0)
                icon_position = intent.getIntExtra("icon_position",0)
                icon_seq = intent.getIntExtra("icon_seq",0)

                color = Color.parseColor(colorDialogItem[icon_color_position].color)
                tilMemoInput.hintTextColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

                memoTitleEditText.setText(intent.getStringExtra("title"))
                memoEditText.setText(intent.getStringExtra("content"))
            }

            lifecycleOwner = this@MemoAddActivity
        }
        colorDialogInterFace = this@MemoAddActivity

        init()
    }

    fun init(){
        //메모 저장
        binding.btOk.setOnClickListener {
            if(activity_type == 0){
                viewmodel.setDataType(1)

                viewmodel.setMemoData(icon_color_position,0, binding.memoTitleEditText.text.toString(), binding.memoEditText.text.toString())
            }
            else if(activity_type == 1){
                viewmodel.setDataType(3)
                viewmodel.adapter_position = icon_position
                viewmodel.updateMemoData(icon_seq = icon_seq.toLong(),icon_color_position = icon_color_position,
                    memo_title = binding.memoTitleEditText.text.toString() , memo_content = binding.memoEditText.text.toString())
            }

            finish()
        }

        //배경색상 정하기
        binding.ivBackgroundColor.setOnClickListener {
            ColorDialog(this).setInterface(colorDialogInterFace,icon_color_position)

        }

        textWatcher()
    }

    //제목 이벤트 처리
    fun textWatcher(){
        binding.memoTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length >= 40) {
                    binding.tilMemoInput.error = getString(R.string.memo_title_error)
                } else {
                    binding.tilMemoInput.error = null
                }
            }

        })

    }


    //배경색상 및 텍스트 색상변경
    override fun itemViewOnClick(interface_color: String, interface_type: Int, data_position: Int) {
        icon_color_position = data_position

        binding.tilMemoInput.boxStrokeErrorColor = ColorStateList.valueOf(Color.parseColor("#B90101"))
        binding.tilMemoInput.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#B90101")))

        if(data_position == 13){
            binding.memoTitleEditText.setTextColor(Color.parseColor("#000000"))
            binding.tilMemoInput.hintTextColor = ColorStateList.valueOf(Color.parseColor("#000000"))
            binding.tilMemoInput.defaultHintTextColor = ColorStateList.valueOf(Color.parseColor("#000000"))
            binding.tilMemoInput.boxStrokeColor = Color.parseColor("#000000")
            binding.memoEditText.setTextColor(Color.parseColor("#000000"))
            binding.memoEditText.setHintTextColor(Color.parseColor("#000000"))
            binding.btOk.setTextColor(Color.parseColor("#000000"))
            binding.ivBackgroundColor.setColorFilter(Color.parseColor("#000000"))
        }
        else{
            binding.memoTitleEditText.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tilMemoInput.hintTextColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.tilMemoInput.defaultHintTextColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.tilMemoInput.boxStrokeColor = Color.parseColor("#FFFFFF")
            binding.memoEditText.setTextColor(Color.parseColor("#FFFFFF"))
            binding.memoEditText.setHintTextColor(Color.parseColor("#FFFFFF"))
            binding.btOk.setTextColor(Color.parseColor("#FFFFFF"))
            binding.ivBackgroundColor.setColorFilter(Color.parseColor("#FFFFFF"))
        }
        binding.color = Color.parseColor(interface_color)
    }
}