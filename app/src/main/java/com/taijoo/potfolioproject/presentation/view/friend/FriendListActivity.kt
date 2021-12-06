package com.taijoo.potfolioproject.presentation.view.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.ActivityFriendListBinding

class FriendListActivity : AppCompatActivity() {

    lateinit var binding : ActivityFriendListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_friend_list)

        binding.apply {

            activity = this@FriendListActivity
            titleAppbar.ivSetting.visibility = View.VISIBLE
            lifecycleOwner = this@FriendListActivity
        }
    }
}