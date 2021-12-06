package com.taijoo.potfolioproject.data.repository.room.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.taijoo.potfolioproject.data.repository.room.Dao.UserDao
import com.taijoo.potfolioproject.data.repository.room.Database.UserDB
import com.taijoo.potfolioproject.data.repository.room.entity.User

class UserRepository(private val userDao: UserDao) {


    fun getAll() : LiveData<User> {
        return userDao.getUserAll().asLiveData()
    }

    fun insert(user: User) {
        userDao.insertUserAll(user)
    }

    fun update(user : User){
        userDao.updateUser(user.user_seq,user.user_name,user.profile,user.user_register_name,user.email,user.network_state)
    }



    fun updateName(name : String){
        userDao.updateUserName(name)
    }

    fun updateProfile(profile : String){
        userDao.updateUserProfile(profile)
    }



    fun updateNetworkState(updateUserNetworkState : Boolean){
        userDao.updateUserNetworkState(updateUserNetworkState)
    }
}