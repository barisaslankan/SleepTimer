package com.barisaslankan.sleeptimer.util

fun Long.toMinutes() : Int {
    return (this/60000).toInt()
}