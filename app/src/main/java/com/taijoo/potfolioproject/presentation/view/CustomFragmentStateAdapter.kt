package com.taijoo.potfolioproject.presentation.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taijoo.potfolioproject.presentation.view.memo.MemoFragment
import com.taijoo.potfolioproject.presentation.view.user.UserFragment

class CustomFragmentStateAdapter (fragmentActivity: FragmentActivity , fragmentArrayList: ArrayList<Fragment>) : FragmentStateAdapter(fragmentActivity){

    private var fragmentArray : ArrayList<Fragment> = ArrayList()


    init {
        this.fragmentArray = fragmentArrayList
    }
    override fun getItemCount(): Int {
        return fragmentArray.size
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> MemoFragment()
            1-> UserFragment()
            else -> MemoFragment()
        }
    }
}