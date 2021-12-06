package com.taijoo.potfolioproject

import android.app.Application
import com.taijoo.potfolioproject.presentation.view.MainActivity
import com.taijoo.potfolioproject.presentation.view.memo.MemoFragment

class MyApplication : Application() {


    init{
        instance = this
    }


    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()

    }

    //메모 프레그먼트
    lateinit var memoFragment: MemoFragment
    lateinit var mainActivity: MainActivity

}