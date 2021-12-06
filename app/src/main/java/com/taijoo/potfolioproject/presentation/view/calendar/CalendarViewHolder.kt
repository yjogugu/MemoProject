package com.taijoo.potfolioproject.presentation.view.calendar

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.CalendarMemoData
import com.taijoo.potfolioproject.databinding.ItemCalendarMemoLayoutBinding

class CalendarViewHolder(var binding : ItemCalendarMemoLayoutBinding , var calendarInterface: CalendarInterface) : RecyclerView.ViewHolder(binding.root) {

    fun bind(calendarMemoData : CalendarMemoData){
        binding.data = calendarMemoData

    }

    fun mainClick(calendarMemoData : CalendarMemoData){
        binding.clMain.setOnClickListener {
            calendarInterface.onClick(calendarMemoData.year,calendarMemoData.month,calendarMemoData.day,calendarMemoData.icon_seq.toInt(),bindingAdapterPosition)
        }
    }
}