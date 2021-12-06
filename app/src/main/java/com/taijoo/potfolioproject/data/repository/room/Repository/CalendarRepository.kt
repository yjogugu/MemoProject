package com.taijoo.potfolioproject.data.repository.room.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.taijoo.potfolioproject.data.model.CalendarData
import com.taijoo.potfolioproject.data.model.CalendarMemoData
import com.taijoo.potfolioproject.data.repository.room.Dao.CalendarDao
import com.taijoo.potfolioproject.data.repository.room.entity.CalendarEntity
import com.taijoo.potfolioproject.data.repository.room.entity.Memo

class CalendarRepository (private val calendarDao: CalendarDao) {

    suspend fun insertDate(calendarEntity: CalendarEntity){
        calendarDao.insertCalendar(calendarEntity)
    }


    fun getMemoSeqData(year : Int , month : Int , day : Int):List<CalendarMemoData>{

        return calendarDao.selectMemo(year, month, day)
    }

    suspend fun deleteCalendarMemo(year : Int , month : Int , day : Int , memo_seq : Int){
        calendarDao.deleteCalendar(year, month, day, memo_seq)
    }

    fun getCalendar():LiveData<List<CalendarData>>{
        return calendarDao.selectCalendar().asLiveData()
    }
}