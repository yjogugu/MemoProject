package com.taijoo.potfolioproject.data.repository.room.Repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.taijoo.potfolioproject.data.repository.room.Dao.MemoDao
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.data.repository.room.entity.User
import com.taijoo.potfolioproject.util.MemoPagingSource
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn


class MemoRepository(private val memoDao: MemoDao) {

    suspend fun insertMemo(memo : Memo){
        memoDao.insertMemo(memo)
    }

    fun updateMemo(icon_position : Int, icon_seq : Long){
        memoDao.updateMemo(icon_position,icon_seq)
    }

    fun updateMemo(icon_seq: Long , icon_color_position : Int , memo_title : String , memo_content : String){
        memoDao.updateMemo(icon_seq, icon_color_position, memo_title, memo_content)
    }

    suspend fun deleteMemo(icon_seq : Long){
        memoDao.deleteMemo(icon_seq)
    }


    fun getMemoDataPaging(start : Int) : Flow<List<Memo>>{

        return memoDao.selectMemoPaging(start,10).conflate()
    }

    fun getMemoDataPagingMainThread(start : Int) : List<Memo>{

        return memoDao.selectMemoPagingMainThread(start,50)
    }


    fun selectMemo(start : Int , end : Int):LiveData<List<Memo>>{
        return memoDao.selectMemoPaging(start,end).asLiveData()
    }

    fun selectMemoFlow(start : Int , end : Int):Flow<List<Memo>>{
        return memoDao.selectMemoPaging(start,end).conflate()
    }

    fun getCount() : Flow<Int>{
        return memoDao.selectMemoCount().flowOn(Dispatchers.Default).conflate()
    }

    fun getMemo() : Flow<PagingData<Memo>>{

        return Pager( config = PagingConfig( pageSize = 10, enablePlaceholders = false ), pagingSourceFactory = { MemoPagingSource(memoDao) } ).flow
    }


}