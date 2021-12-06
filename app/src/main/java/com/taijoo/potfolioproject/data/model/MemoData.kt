package com.taijoo.potfolioproject.data.model


class MemoData {
    var type = 0
    var count = 0

    constructor(type : Int) {}

    constructor(type: Int, count: Int) {
        this.type = type
        this.count = count

    }
}