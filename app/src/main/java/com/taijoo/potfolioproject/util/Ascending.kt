package com.taijoo.potfolioproject.util

class Ascending : Comparator<Int> {
    override fun compare(p0: Int?, p1: Int?): Int {

        return p0!!.compareTo(p1!!)
    }

}
