package com.taijoo.potfolioproject.presentation.view.memo

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.Pair
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.databinding.ItemMemoLayoutBinding
import com.taijoo.potfolioproject.util.InterFace.MemoDeleteClickInterface

class MemoViewHolder(var binding : ItemMemoLayoutBinding ,var memoDeleteClickInterface : MemoDeleteClickInterface) : RecyclerView.ViewHolder(binding.root)  {


    fun binding(memo: Memo?, remove_type : Int){

        binding.item = memo

        if(memo?.icon_color_position == 13){
            binding.iconTitleTextview.setTextColor(Color.parseColor("#000000"))
        }
        else{
            binding.iconTitleTextview.setTextColor(Color.parseColor("#FFFFFF"))
        }

        //삭제 아이콘 활성화 여부
        if(remove_type == 1){
            binding.ivRemove.visibility  = View.VISIBLE
        }
        else{
            binding.ivRemove.isChecked = false
            binding.ivRemove.visibility = View.GONE
        }

        binding.ivRemove.setOnCheckedChangeListener(null)
        //삭제 체크값이 0이 아니면 체크 해제
        binding.ivRemove.isChecked = memo?.icon_delete_check != 0


        binding.ivRemove.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                memo?.icon_delete_check = 1
            }
            else{
                memo?.icon_delete_check = 0
            }

            memoDeleteClickInterface.itemViewOnClick(0,bindingAdapterPosition , isChecked)
        }
    }


    fun memoOnClick(context: Context, view: View, view2: View, memo: Memo?, remove_type: Int){
        binding.memoFrameLayout.setOnClickListener {
            if(remove_type == 1){
                binding.ivRemove.isChecked = !binding.ivRemove.isChecked
            }
            else{
                memoDeleteClickInterface.itemViewOnClick(1)
                val intent = Intent(context, MemoClickCustomDialogActivity::class.java)
                intent.putExtra("icon_position",memo?.icon_position)

                val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                        Pair.create(view, context.getString(R.string.icon_animation)),
                        Pair.create(view2, context.getString(R.string.icon_main_animation)))
                ActivityCompat.startActivity(context, intent, options.toBundle())
            }

        }


    }



}