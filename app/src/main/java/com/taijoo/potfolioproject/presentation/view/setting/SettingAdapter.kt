package com.taijoo.potfolioproject.presentation.view.setting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.SettingData
import com.taijoo.potfolioproject.databinding.ItemSettingLayoutBinding
import com.taijoo.potfolioproject.databinding.ItemTestLayoutBinding

class SettingAdapter(var arrayList: ArrayList<SettingData> , var settingInterface: SettingInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemSettingBinding: ItemSettingLayoutBinding = ItemSettingLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return SettingViewHolder(itemSettingBinding,settingInterface)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val settingData = arrayList[position]

        if(holder is SettingViewHolder){
            holder.bind(settingData)

            holder.binding.clItem.setOnClickListener {
                holder.onClick(settingData.icon_type)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    fun setData(arrayList: ArrayList<SettingData> , type : Int){
        this.arrayList = arrayList

        when(type){
            //최초생성
            0 ->{
                notifyItemRangeInserted(1,itemCount)
            }
            //서버 연결상태 변경 = 연결되어 있을때
            1 ->{
                notifyItemRangeInserted(2,itemCount)
                notifyItemChanged(1,false)
            }
            //서버 연결상태 변경 = 연결 안되어 있을때
            2 ->{
                notifyItemRangeRemoved(2,itemCount)
                notifyItemChanged(1,false)
            }
        }


    }

}