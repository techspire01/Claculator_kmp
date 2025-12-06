package com.example.claculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform