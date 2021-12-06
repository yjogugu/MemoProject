package com.taijoo.potfolioproject.presentation.view.user

import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.repository.room.entity.User
import com.taijoo.potfolioproject.databinding.ItemUserLayoutBinding

class UserViewHolder(val binding : ItemUserLayoutBinding) : RecyclerView.ViewHolder(binding.root) {


    fun binding(user: User){
        binding.userName = user.user_name
        binding.userProfile = user.profile
    }

    fun init(userClickInterface: UserClickInterface , position : Int){
        binding.ivProfile.setOnClickListener {
            userClickInterface.UserOnClick(position)

        }
    }
}