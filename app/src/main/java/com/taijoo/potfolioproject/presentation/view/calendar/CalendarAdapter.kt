package com.taijoo.potfolioproject.presentation.view.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.CalendarMemoData
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.databinding.ItemCalendarMemoLayoutBinding


class CalendarAdapter(val calendarInterface: CalendarInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var memoItem : MutableList<CalendarMemoData> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(memoItem : MutableList<CalendarMemoData>){
        this.memoItem = memoItem
        notifyDataSetChanged()
    }

    fun setAdd(memoItem : CalendarMemoData){
        this.memoItem.add(0,memoItem)
        notifyItemInserted(0)
    }

    fun setRemove(position: Int){
        this.memoItem.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,itemCount)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : ItemCalendarMemoLayoutBinding = ItemCalendarMemoLayoutBinding.inflate(LayoutInflater.from(parent.context),parent , false)
        return CalendarViewHolder(view,calendarInterface)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = memoItem[position]

        if(holder is CalendarViewHolder){
            holder.bind(item)

            holder.mainClick(item)
        }
    }

    override fun getItemCount(): Int {
        return memoItem.size
    }

}