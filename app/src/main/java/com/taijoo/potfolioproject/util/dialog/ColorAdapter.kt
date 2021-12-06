package com.taijoo.potfolioproject.util.dialog

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.databinding.ItemLayoutColorBinding
import com.taijoo.potfolioproject.util.InterFace.ColorDialogInterFace
import com.taijoo.potfolioproject.util.ColorDialogItem

class ColorAdapter(var context: Context,var color_position : Int ,  var mdata : ArrayList<ColorDialogItem> , var custominterface : ColorDialogInterFace)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){//큐브 아이콘 색상 변경 어뎁터

    init {
        mdata.get(color_position).check = true
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : ItemLayoutColorBinding = ItemLayoutColorBinding.inflate(LayoutInflater.from(parent.context),parent , false)
        return Color_ViewHolder(view)
    }

    override fun getItemCount():Int{
        return mdata.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is Color_ViewHolder){

            holder.bind(mdata.get(position))

            holder.colorClick(View.OnClickListener {

                mdata.get(position).check = true//선택한 컬러 View
                notifyItemChanged(position,false)

                mdata.get(color_position).check = false//이전 선택헀던 컬러 View 해제
                notifyItemChanged(color_position,false)

                color_position = position

                custominterface.itemViewOnClick(mdata.get(color_position).color!!,1,mdata.get(color_position).position)

            })

        }

    }


    class Color_ViewHolder(val binding : ItemLayoutColorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data : ColorDialogItem){

            binding.imageviewMainImage1.setBackgroundColor(Color.parseColor(data.color))

            if(data.check){
                if(data.position == 13){
                    binding.colorCheckImageview.setColorFilter(Color.parseColor("#000000"))
                }
                else{
                    binding.colorCheckImageview.setColorFilter(Color.parseColor("#FFFFFF"))
                }
                binding.colorCheckImageview.visibility = View.VISIBLE
            }
            else{
                binding.colorCheckImageview.visibility = View.GONE
            }

        }

        fun colorClick(onClickListener: View.OnClickListener){
            binding.colorClickLinearLayout.setOnClickListener(onClickListener)
        }

    }
}