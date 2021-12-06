package com.taijoo.potfolioproject.util.InterFace

interface MemoDeleteClickInterface {
    //type = 0:삭제 , 1:Item 클릭시 액션플로팅버튼 감추기
    fun itemViewOnClick(type : Int)

    fun itemViewOnClick(type : Int ,position : Int , isCheck : Boolean)
}