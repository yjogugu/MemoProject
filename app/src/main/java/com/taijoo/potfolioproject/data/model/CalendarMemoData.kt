package com.taijoo.potfolioproject.data.model

import com.taijoo.potfolioproject.data.repository.room.entity.CalendarEntity
import com.taijoo.potfolioproject.data.repository.room.entity.Memo

data class CalendarMemoData(val icon_seq : Long,val memo_title:String,val memo_content:String,val icon_color_position:Int, val year : Int, val month : Int, val day : Int)

data class CalendarData(val year : Int, val month : Int, val day : Int)