package com.hydbest.baseandroid.kt

import android.os.Build
import java.time.LocalDate
import java.time.Period

fun String.lastChar(): Char = this.get(this.length - 1)

val Int.days: Int
    get() = 10

fun String.order(sum: (Int, Int) -> String, a: Int, b: Int): String {
    return sum(a,b)
}


// 声明接收者
fun kotlinDSL(block:StringBuilder.()->Unit){
    block(StringBuilder("Kotlin"))
}

