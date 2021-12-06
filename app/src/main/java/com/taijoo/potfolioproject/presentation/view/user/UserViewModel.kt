package com.taijoo.potfolioproject.presentation.view.user

import android.app.Application
import androidx.lifecycle.*
import com.taijoo.potfolioproject.data.model.UserData

import com.taijoo.potfolioproject.data.repository.room.entity.User
import com.taijoo.potfolioproject.data.repository.room.Repository.UserRepository
import com.taijoo.potfolioproject.util.ListLiveData


class UserViewModel(application: Application) : AndroidViewModel(application) {

//    private val repository : UserRepository = UserRepository(application)

    private  var user_Data : ListLiveData<UserData> = ListLiveData<UserData>()

    init {

//        repository.getAll().observeForever{
//
//            user_Data.add(UserData(0,0,it))
//
//        }

    }


//    fun getUser():LiveData<User>{
////        return repository.getAll()
//    }

    fun getData(): ListLiveData<UserData> {

        return user_Data

    }


    fun InsertUser(position : Int){
//        viewModelScope.launch {
//            user_Data.remove(position)
//            withContext(Dispatchers.IO){
//                repository.insert(User(0,"qq1333","Profile/Image/user_profile.jpg"))
//
//            }
//
//        }

    }




}