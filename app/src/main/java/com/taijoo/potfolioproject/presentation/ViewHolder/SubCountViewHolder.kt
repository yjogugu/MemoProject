package com.taijoo.potfolioproject.presentation.ViewHolder

import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.databinding.ItemSubCountLayoutBinding

class SubCountViewHolder(var binding : ItemSubCountLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun binding (text : String){
        binding.data = text
    }
}