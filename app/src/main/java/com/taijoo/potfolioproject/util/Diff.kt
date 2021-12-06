package com.taijoo.potfolioproject.util

import androidx.recyclerview.widget.DiffUtil
import com.taijoo.potfolioproject.data.model.UserData

class Diff(
        private val oldData: List<Any>,
        private val newData: List<Any>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]

        return if (oldItem is UserData && newItem is UserData) {
            oldItem == newItem
        } else {
            false
        }
    }

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldData[oldItemPosition] == newData[newItemPosition]
}