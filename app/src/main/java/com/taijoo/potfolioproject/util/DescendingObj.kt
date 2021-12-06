package com.taijoo.potfolioproject.util

class DescendingObj : Comparator<Int>{
    override fun compare(p0: Int?, p1: Int?): Int {
        return p1!!.compareTo(p0!!)
    }

}