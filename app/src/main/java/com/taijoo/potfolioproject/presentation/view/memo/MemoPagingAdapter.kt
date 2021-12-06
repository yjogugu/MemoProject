package com.taijoo.potfolioproject.presentation.view.memo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.databinding.ItemMemoLayoutBinding
import com.taijoo.potfolioproject.databinding.ItemMemoLinearLayoutBinding
import com.taijoo.potfolioproject.util.InterFace.ItemTouchHelperListener
import com.taijoo.potfolioproject.util.InterFace.MemoDeleteClickInterface

class MemoPagingAdapter(val context: Context, private val layoutManager: GridLayoutManager, var memoDeleteClickInterface: MemoDeleteClickInterface )
    :PagingDataAdapter<Memo , RecyclerView.ViewHolder>(Memo_DIFF) , ItemTouchHelperListener {

    var remove_type = 0// 삭제 버튼 0:비활성화 , 1활성화

    enum class ViewType{//ViewType 나누기 ONE = 1배율 , MULTI 나머지
        ONE, MULTI
    }

    companion object {
        private val Memo_DIFF = object: DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.icon_seq == newItem.icon_seq
            }

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)


        if(holder is MemoViewHolder){

//            holder.binding(item, remove_type)

            holder.memoOnClick(context, holder.binding.iconTitleTextview, holder.binding.memoFrameLayout, item, remove_type )//메모 클릭

        }
        else if(holder is MemoLinearViewHolder){
//            holder.binding(item, remove_type )

            holder.memoOnClick(context, holder.binding.iconTitleTextview, holder.binding.memoFrameLayout, item, remove_type )//메모 클릭

//            holder.memoDelete(memoDeleteClickInterface)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType){
            ViewType.MULTI.ordinal -> {//Grid View
                val itemMainBinding: ItemMemoLayoutBinding = ItemMemoLayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false)

                return MemoViewHolder(itemMainBinding,memoDeleteClickInterface)
            }
            ViewType.ONE.ordinal -> {//Linear View
                val itemMainBinding: ItemMemoLinearLayoutBinding = ItemMemoLinearLayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false)

                return MemoLinearViewHolder(itemMainBinding ,memoDeleteClickInterface)
            }
            else -> throw IllegalStateException("ProfileDetails_Adapter View Type Create Error")
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(layoutManager.spanCount == 1){
            return MemoAdapter.ViewType.ONE.ordinal
        }
        else{
            return MemoAdapter.ViewType.MULTI.ordinal
        }
    }

    override fun onItemMove(from_position: Int, to_position: Int, viewHolder: RecyclerView.ViewHolder?): Boolean {

        if(getItem(from_position)!!.icon_delete_type == 0){
            //이동할 객체 저장
//            val iconItem: Memo = item[from_position]
//            //이동할 객체 삭제
//            item.removeAt(from_position)
//            //이동하고 싶은 포지션에 추가
//            item.add(to_position, iconItem)

            notifyItemMoved(from_position, to_position)
            notifyItemRangeChanged(from_position,to_position)
            return true
        }
        else{
            return false
        }
    }


    fun setRemoveType(type : Int){
        this.remove_type = type
        notifyDataSetChanged()
    }
}