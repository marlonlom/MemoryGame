package com.example.memorygame

interface BoardListener {
    fun onWinner()
    fun onMatchEvent()
    fun onWrongMatch()
}
