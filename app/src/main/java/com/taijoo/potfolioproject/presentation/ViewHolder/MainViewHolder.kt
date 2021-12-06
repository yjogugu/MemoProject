package com.taijoo.potfolioproject.presentation.ViewHolder

import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.databinding.ItemMemoLayoutBinding

class MainViewHolder(var binding : ItemMemoLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    //레이아웃 데이터 바인딩 부분
//    fun binding(memo: Memo? , remove_type : Int){
//        if(memo?.icon_color_position == 13){
//            binding.iconTitleTextview.setTextColor(Color.parseColor("#000000"))
//        }
//        else{
//            binding.iconTitleTextview.setTextColor(Color.parseColor("#FFFFFF"))
//        }
//        binding.backgroundColor = memo?.icon_color_position
//        binding.title = memo?.memo_title.toString()
//
//        //삭제 아이콘 활성화 여부
//        if(remove_type == 1){
//            binding.ivRemove.visibility  = View.VISIBLE
//        }
//        else{
//            memo?.icon_delete_check = 0
//            binding.ivRemove.visibility = View.GONE
//        }
//
//    }
//
//
//    fun memoOnClick(context: Context, view: View, view2: View , memo: Memo? , remove_type: Int){
//        binding.memoFrameLayout.setOnClickListener {
//            if(remove_type == 1){
//                binding.ivRemove.isChecked = !binding.ivRemove.isChecked
//            }
//            else{
//                val intent = Intent(context, MemoClickCustomDialogActivity::class.java)
//                intent.putExtra("icon_color_position",memo?.icon_color_position)
//                intent.putExtra("title",memo?.memo_title)
//                intent.putExtra("content",memo?.memo_content)
//                intent.putExtra("date",memo?.date)
//                val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
//                        Pair.create(view, context.getString(R.string.icon_animation)),
//                        Pair.create(view2, context.getString(R.string.icon_main_animation)))
//                ActivityCompat.startActivity(context, intent, options.toBundle())
//            }
//
//        }
//
//    }
}