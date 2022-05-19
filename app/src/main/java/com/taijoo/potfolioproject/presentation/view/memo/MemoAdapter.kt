package com.taijoo.potfolioproject.presentation.view.memo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.databinding.ItemMemoLayoutBinding
import com.taijoo.potfolioproject.databinding.ItemMemoLinearLayoutBinding
import com.taijoo.potfolioproject.util.Ascending
import com.taijoo.potfolioproject.util.DescendingObj
import com.taijoo.potfolioproject.util.InterFace.ItemTouchHelperListener
import com.taijoo.potfolioproject.util.InterFace.MemoDeleteClickInterface
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList


class MemoAdapter(val context: Context, private val layoutManager: GridLayoutManager, var memoDeleteClickInterface: MemoDeleteClickInterface)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperListener {

    var item : MutableList<Memo> = ArrayList()
    var remove_type = 0// 삭제 버튼 0:비활성화 , 1활성화


    enum class ViewType{//ViewType 나누기 ONE = 1배율 , MULTI 나머지
        ONE, MULTI
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType){
            ViewType.MULTI.ordinal -> {//Grid View
                val itemMainBinding: ItemMemoLayoutBinding = ItemMemoLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return MemoViewHolder(itemMainBinding , memoDeleteClickInterface)
            }
            ViewType.ONE.ordinal -> {//Linear View
                val itemMainBinding: ItemMemoLinearLayoutBinding =
                    ItemMemoLinearLayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )

                return MemoLinearViewHolder(itemMainBinding,memoDeleteClickInterface)
            }
            else -> throw IllegalStateException("ProfileDetails_Adapter View Type Create Error")
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val _item  = item[holder.bindingAdapterPosition]

        if(holder is MemoViewHolder){

            holder.binding(_item, remove_type)

            holder.memoOnClick(context, holder.binding.iconTitleTextview, holder.binding.memoFrameLayout, _item, remove_type)//메모 클릭

        }
        else if(holder is MemoLinearViewHolder){
            holder.binding(_item, remove_type)

            holder.memoOnClick(context, holder.binding.iconTitleTextview, holder.binding.memoFrameLayout, _item, remove_type )//메모 클릭

        }


    }

    override fun getItemCount(): Int {
       return item.size
    }


    override fun getItemId(position: Int): Long {
//        return super.getItemId(position)
        return item[position].icon_seq
    }


    //데이터 삭제
    fun removeItem(position: ArrayList<Int>){

        Collections.sort(position,DescendingObj())//포지션 저장한 List 내림차순으로 정렬

        for (data in position){
            this.item.removeAt(data)
            notifyItemRemoved(data)
            notifyItemRangeChanged(data, itemCount)

        }

        this.remove_type = 0
        notifyItemRangeChanged(0, itemCount, false)

    }

    //데이터 가져오기
    @SuppressLint("NotifyDataSetChanged")
    fun setData(item: MutableList<Memo>){
        this.item = item
        notifyDataSetChanged()
    }

    fun setInsert(item: Memo){
        val subList = this.item
        subList.add(0, item)

        this.item = subList
        notifyItemInserted(0)
    }

    fun updateData(item: Memo , position: Int){
        this.item[position] = item
        notifyItemChanged(position)
    }


    //페이징 전용 메소드
    fun setPaging(item: MutableList<Memo>){
        if(item.size > 0){
            val position = this.item.size

            this.item.addAll(item)

            notifyItemRangeInserted(position, this.item.size)

        }

    }

    //삭제체크박스 활성화 여부
    fun setRemoveType(remove_type: Int){
        this.remove_type = remove_type
        notifyItemRangeChanged(0, this.item.size, true)

    }

    override fun getItemViewType(position: Int): Int {
        if(layoutManager.spanCount == 1){
            return ViewType.ONE.ordinal
        }
        else{
            return ViewType.MULTI.ordinal
        }
    }


    override fun onItemMove(
        from_position: Int,
        to_position: Int,
        viewHolder: RecyclerView.ViewHolder?
    ): Boolean {
        if(item.get(from_position).icon_delete_type == 0){
            //이동할 객체 저장
            val iconItem: Memo = item.get(from_position)
            //이동할 객체 삭제
            this.item.removeAt(from_position)
            //이동하고 싶은 포지션에 추가
            this.item.add(to_position, iconItem)

            notifyItemMoved(from_position, to_position)
            return true
        }
        else{
            return false
        }

    }
}