package com.example.myfirtsapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform