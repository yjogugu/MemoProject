package com.taijoo.potfolioproject.presentation.view.friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
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

        init()
    }


    fun init(){

        binding.titleAppbar.ivSetting.setOnClickListener {
            val popup = PopupMenu(this, binding.titleAppbar.ivSetting)

            popup.inflate(R.menu.friendsettingmenu)

            popup.setOnMenuItemClickListener{item ->
                when(item.itemId){
                    R.id.friend_add -> {

                    }
                }
                true
            }

            popup.show()
        }
    }
}