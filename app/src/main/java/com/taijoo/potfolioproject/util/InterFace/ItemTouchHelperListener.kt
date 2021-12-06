package com.taijoo.potfolioproject.util.InterFace

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {

    fun onItemMove(from_position: Int, to_position: Int, viewHolder: RecyclerView.ViewHolder?): Boolean
}