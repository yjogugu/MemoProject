package com.taijoo.potfolioproject.data.model

import com.taijoo.potfolioproject.data.repository.room.entity.Memo


//인터페이스로 리사이클러뷰 뷰타입 지정

//data class MainData(val result: Boolean, val sub_data: ArrayList<MainSubData>? ) {
////    constructor() : this(false,null)
//    constructor(result: Boolean, sub_data: MainSubData) : this(false,null)
//
//}


class MainData {

    var result = false
//    var sub_data : List<MainSubData>? = null
    var type = 0
    var mainSubData2: MainSubData2? = null
    var memo: Memo? = null

    constructor(type : Int, result: Boolean, memo: Memo){
        this.type = type
        this.result = result
        this.memo = memo
    }

    constructor(type: Int , mainSubData2: MainSubData2 ){
        this.type = type
        this.mainSubData2 = mainSubData2

    }

}



data class MainSubData(val count : Int)  {
    constructor() : this(0)

}


data class MainSubData2(val count1 : Int) {
    constructor() : this(0)
}

