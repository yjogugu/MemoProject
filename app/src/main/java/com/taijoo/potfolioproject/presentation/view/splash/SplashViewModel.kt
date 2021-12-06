package com.taijoo.potfolioproject.presentation.view.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.taijoo.potfolioproject.data.repository.Http.Repository
import com.taijoo.potfolioproject.data.repository.room.Repository.UserRepository


class SplashViewModel(application: Application) : AndroidViewModel(application) {

//    val repository : UserRepository = UserRepository(application)

    val api = Repository()


    //유저 데이터 서버에서 받아와서 Room 저장
    fun InsertUser(){
//        api.getUserInfo()
    }


}