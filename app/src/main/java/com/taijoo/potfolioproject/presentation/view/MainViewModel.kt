package com.taijoo.potfolioproject.presentation.view

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel


import com.taijoo.potfolioproject.data.repository.Http.Repository
import com.taijoo.potfolioproject.data.repository.room.Repository.UserRepository
import com.taijoo.potfolioproject.presentation.view.memo.MemoFragment
import com.taijoo.potfolioproject.presentation.view.user.UserFragment

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var api = Repository()

//    val repository : UserRepository = UserRepository(application)

    private var fragmentArray : ArrayList<Fragment> = ArrayList()


    init {
//        api.getUserInfo(repository,this@MainViewModel)

        fragmentArray.add(MemoFragment())
        fragmentArray.add(UserFragment())


    }



    fun getMainFragmentArray():ArrayList<Fragment>{

        return fragmentArray
    }



    companion object {
        private const val TAG = "MainViewModel"
    }

}
