package com.taijoo.potfolioproject.presentation.view.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.taijoo.potfolioproject.data.model.CalendarData
import com.taijoo.potfolioproject.data.model.CalendarMemoData
import com.taijoo.potfolioproject.data.repository.room.Dao.CalendarDao
import com.taijoo.potfolioproject.data.repository.room.Dao.MemoDao
import com.taijoo.potfolioproject.data.repository.room.Database.UserDB
import com.taijoo.potfolioproject.data.repository.room.Repository.CalendarRepository
import com.taijoo.potfolioproject.data.repository.room.Repository.MemoRepository
import com.taijoo.potfolioproject.data.repository.room.entity.CalendarEntity
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val userDatabase = UserDB.getInstance(application)!!
    private val calendarDao: CalendarDao = userDatabase.calendarDao()
    private val memoDao: MemoDao = userDatabase.memoDao()

    private val calendarDaoApi : CalendarRepository = CalendarRepository(calendarDao)
    private val memoDaoApi : MemoRepository = MemoRepository(memoDao)

    private lateinit var  memoData : LiveData<List<CalendarEntity>>

    fun insertCalendar(calendarEntity: CalendarEntity){
        viewModelScope.launch {
            calendarDaoApi.insertDate(calendarEntity)
        }
    }

    fun getMemo(year : Int , month : Int , day : Int):List<CalendarMemoData>{

        return  calendarDaoApi.getMemoSeqData(year, month, day)
    }

    fun deleteCalendarMemo(year : Int , month : Int , day : Int,memo_seq : Int){
        viewModelScope.launch {
            calendarDaoApi.deleteCalendarMemo(year, month, day, memo_seq)
        }
    }

    fun getCalendar():LiveData<List<CalendarData>>{
        return calendarDaoApi.getCalendar()
    }
}