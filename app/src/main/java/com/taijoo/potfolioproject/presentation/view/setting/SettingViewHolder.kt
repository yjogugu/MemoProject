package com.taijoo.potfolioproject.presentation.view.setting

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.SettingData
import com.taijoo.potfolioproject.databinding.ItemSettingLayoutBinding


class SettingViewHolder (var binding : ItemSettingLayoutBinding, private val settingInterface: SettingInterface) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: SettingData){
        binding.data = data

        if(data.icon_type == 1){
            binding.switchNetwork.visibility = View.VISIBLE
            binding.switchNetwork.isChecked = data.networkState//스위치 네트워크 상태유무
        }
        else{
            binding.switchNetwork.visibility = View.GONE
        }

        binding.switchNetwork.setOnClickListener {
            settingInterface.onClick(data.icon_type)
        }

//        binding.switchNetwork.setOnCheckedChangeListener { compoundButton, b ->
//            settingInterface.onClick(data.icon_type)
//        }
    }

    fun onClick(icon_type : Int){
        binding.switchNetwork.isChecked = !binding.switchNetwork.isChecked
        settingInterface.onClick(icon_type)
    }
}