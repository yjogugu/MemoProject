package com.taijoo.potfolioproject.presentation.view.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.UserData

import com.taijoo.potfolioproject.databinding.ItemSubLayoutBinding


import com.taijoo.potfolioproject.databinding.ItemUserLayoutBinding
import com.taijoo.potfolioproject.presentation.ViewHolder.SubViewHolder


class UserAdapter(userClickInterface: UserClickInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var userList : List<UserData>

    private val userClickInterface: UserClickInterface

    companion object {
        const val VIEW_TYPE_PAGE_0 = 0                                                         //가로
        const val VIEW_TYPE_PAGE_1 = 1                                                         //세로

    }

    init {
        this.userClickInterface = userClickInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            VIEW_TYPE_PAGE_0 ->{
                val itemMainBinding : ItemUserLayoutBinding = ItemUserLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),parent , false)

                return UserViewHolder(itemMainBinding)
            }
            VIEW_TYPE_PAGE_1 ->{
                val itemSubViewHolder : ItemSubLayoutBinding = ItemSubLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),parent , false)

                return SubViewHolder(itemSubViewHolder)
            }

            else -> throw IllegalStateException("ProfileDetails_Adapter View Type Create Error")

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val _item  = userList[position]


        if(holder is UserViewHolder){
            holder.binding(_item.user!!)
            holder.init(userClickInterface,position)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    override fun getItemId(position: Int): Long {
//        return super.getItemId(position)
        return userList.get(position).id
    }
    override fun getItemViewType(position: Int): Int {
        when(userList[position].type){//인터페이스 로 뷰타입 나눔
             0 -> {
                return VIEW_TYPE_PAGE_0
            }
            1 ->{
                return VIEW_TYPE_PAGE_1
            }
            else ->{
                throw IllegalStateException("Not Found ViewHolder Type")
            }

        }
    }
}