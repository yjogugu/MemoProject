package com.taijoo.potfolioproject.util.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.presentation.view.memo.MemoFragment
import com.taijoo.potfolioproject.presentation.view.memo.memoAdd.MemoAddActivity
import com.taijoo.potfolioproject.util.SPreference

class MemoDialog(private var context: Context, private var fragment: MemoFragment) {

    //메모 프래그먼트 팝업
    @SuppressLint("UseRequireInsteadOfGet")
    fun MemoPopup(sPreference : SPreference){
        val popup = PopupMenu(context, fragment.binding.ibMemoAddStart)

        popup.inflate(R.menu.memomenu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.tab_add -> {//추가
                    val intent = Intent(context,MemoAddActivity::class.java)
                    intent.putExtra("icon_position",fragment.binding.rvMemo.adapter?.itemCount)
                    context.startActivity(intent)
                }
                R.id.tab_remove -> {//삭제
                    fragment.adapter.setRemoveType(1)
                    fragment.binding.tvOk.visibility = View.VISIBLE
                    fragment.binding.ibMemoAddStart.visibility = View.INVISIBLE

//                    IconRemove(1)
                }
                R.id.scaling -> {//배율
                    SpanCountshowPopup(sPreference)
                }

            }

            true
        })

        popup.show()

    }



    //리사이클러뷰 Span 갯수 팝업 띄우기
    private fun SpanCountshowPopup(sPreference : SPreference) {
        val popup = PopupMenu(context, fragment.binding.ibMemoAddStart)

        var spanCount : Int
        val now_span_count : Int

        //적용할 배수
        if(sPreference.getIntValue("span_count") == -11){
            spanCount = 4
        }
        else{
            spanCount = sPreference.getIntValue("span_count")
        }

        //현재 배수
        if(sPreference.getIntValue("now_span_count") == -11){
            now_span_count = 4
        }
        else{
            now_span_count = sPreference.getIntValue("now_span_count")
        }

        popup.inflate(R.menu.scalingmenu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.span_count_1 -> {
                    sPreference.putValue("span_count",1)
                    spanCount = sPreference.getIntValue("span_count")

                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",1)

                }
                R.id.span_count_2 -> {
                    sPreference.putValue("span_count",2)
                    spanCount = sPreference.getIntValue("span_count")

                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",2)
                }
                R.id.span_count_3 -> {
                    sPreference.putValue("span_count",3)
                    spanCount = sPreference.getIntValue("span_count")
                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",3)
                }
                R.id.span_count_4 -> {
                    sPreference.putValue("span_count",4)
                    spanCount = sPreference.getIntValue("span_count")
                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",4)
                }
                R.id.span_count_5 -> {
                    sPreference.putValue("span_count",5)
                    spanCount = sPreference.getIntValue("span_count")
                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",5)
                }

            }

            true
        })

        popup.show()
    }

    //배율 조정하기
    fun Toggle(count: Int){
        val spanCount = if (4 == count) 4 else count
        fragment.binding.rvMemo.post {

            (fragment.binding.rvMemo.layoutManager as GridLayoutManager).spanCount = spanCount

            fragment.adapter.notifyItemRangeChanged(0, fragment.adapter.itemCount)


        }

    }
}