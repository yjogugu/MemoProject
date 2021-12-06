package com.taijoo.potfolioproject.presentation.view.memo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MemoViewModelFactory(var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            return MemoViewModel(application ) as T
        }

        throw IllegalAccessException("unknow view model class")
    }

}