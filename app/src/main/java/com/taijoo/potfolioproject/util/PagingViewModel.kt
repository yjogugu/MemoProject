package com.taijoo.potfolioproject.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.taijoo.potfolioproject.data.repository.room.Dao.MemoDao
import com.taijoo.potfolioproject.data.repository.room.Database.UserDB
import com.taijoo.potfolioproject.data.repository.room.Repository.MemoRepository

class PagingViewModel(application: Application) : AndroidViewModel(application) {

    private val userDatabase = UserDB.getInstance(application)!!
    private val memoDao: MemoDao = userDatabase.memoDao()

    private val memo_api : MemoRepository = MemoRepository(memoDao)


}