package com.taijoo.potfolioproject.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModel

class ViewModelFactory(private var app : Application?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            return MemoViewModel(app!! ) as T
        }

        throw IllegalAccessException("unknow view model class")
    }


}