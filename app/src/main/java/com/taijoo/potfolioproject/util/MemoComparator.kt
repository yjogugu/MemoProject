package com.taijoo.potfolioproject.util

import androidx.recyclerview.widget.DiffUtil
import com.taijoo.potfolioproject.data.repository.room.entity.Memo

object MemoComparator : DiffUtil.ItemCallback<Memo>() {

    override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return oldItem.icon_seq == newItem.icon_seq
    }

    override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
        return  oldItem == newItem
    }
}