package com.taijoo.potfolioproject.presentation.view.calendar

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.model.CalendarMemoData
import com.taijoo.potfolioproject.data.repository.room.entity.CalendarEntity
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.databinding.ItemCalendarLayoutBinding
import com.taijoo.potfolioproject.util.CurrentDayDecorator
import com.taijoo.potfolioproject.util.dialog.CustomDefaultDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarDialog : DialogFragment,CalendarInterface {


    private lateinit var binding : ItemCalendarLayoutBinding

    private val array = ArrayList<CalendarDay>()

    private lateinit var viewModel : CalendarViewModel

    private var memo : Memo? = null

    private var viewType = 0 // 1.MainActivity , 2.MemoClickDialog

    private lateinit var calendarAdapter : CalendarAdapter

    private var oldDate : String = ""
    constructor(viewType : Int){
        this.viewType = viewType
    }

    constructor(viewType : Int,memo : Memo){
        this.viewType = viewType
        this.memo = memo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemCalendarLayoutBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f

        init()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        //프래그먼트 다이얼로그 가로 사이즈 조정
        val width = (context!!.resources.displayMetrics.widthPixels)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = (width * 1).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    fun init(){

        viewModel.getCalendar().observe(this,{
            for (data in it){
                if (data.year != 0){
                    binding.calendarView.addDecorator(CurrentDayDecorator(Collections.singleton(CalendarDay.from(data.year,  data.month, data.day))))
                }

            }
        })

        calendarAdapter = CalendarAdapter(this)
        binding.rvMemo.setHasFixedSize(true)
        binding.rvMemo.layoutManager = LinearLayoutManager(context)

        binding.rvMemo.adapter = calendarAdapter


        for(i in array){
            binding.calendarView.addDecorator(CurrentDayDecorator(Collections.singleton(i)))

        }

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->

            if(viewType == 2){//메모안에서 캘린더 클릭시


                if (oldDate == date.toString()){//캘린더 한번더 클릭했을경우 저장 여부 물어보기

                    val customDefaultDialog : CustomDefaultDialog = CustomDefaultDialog(context!!,getString(
                        R.string.CustomDialogCalendarTitle),getString(R.string.CustomDialogCalendarContentSave))

                    customDefaultDialog.setDialogListener(object :CustomDefaultDialog.CustomDialogListener{
                        override fun onCheckClick() {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

                            val nowDate = Date()
                            val strDate: String = dateFormat.format(nowDate)

                            viewModel.insertCalendar(CalendarEntity(0, memo?.icon_seq!!,date.year,date.month,date.day,strDate))

                            calendarAdapter.setAdd(CalendarMemoData(memo!!.icon_seq,memo!!.memo_title,memo!!.memo_content,memo!!.icon_color_position
                                ,date.year,date.month,date.day))
                        }

                        override fun onNoClick() {
                        }

                    })


                    if(calendarAdapter.itemCount < 1){//어뎁터 아이템이 하나도 없을떄
                        customDefaultDialog.show()
                    }
                    else{//어뎁터 아이템이 하나 이상 있을때 해당 메모 아이템이 이미 저장되어있는지 중복 확인
                        for (i in calendarAdapter.memoItem){
                            if(i.icon_seq == memo!!.icon_seq){
                                break
                            }
                            else{
                                customDefaultDialog.show()
                            }

                        }
                    }

                }

            }

            viewLifecycleOwner.lifecycleScope.launch {
                val item = viewModel.getMemo(date.year,date.month,date.day) as ArrayList<CalendarMemoData>
                calendarAdapter.setData(item)
            }

            oldDate = date.toString()//캘린더 데이터 저장

            binding.llTest.visibility = View.VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        oldDate = ""
    }

    override fun onClick(year: Int, month: Int, day: Int, memo_seq: Int , position:Int) {
        val customDefaultDialog  = CustomDefaultDialog(context!!,getString(
            R.string.CustomDialogCalendarTitle),getString(R.string.CustomDialogCalendarContentDelete))


        customDefaultDialog.setDialogListener(object :CustomDefaultDialog.CustomDialogListener{
            override fun onCheckClick() {
                viewModel.deleteCalendarMemo(year, month, day, memo_seq)
                calendarAdapter.setRemove(position)
            }

            override fun onNoClick() {
            }

        })
        customDefaultDialog.show()
    }
}