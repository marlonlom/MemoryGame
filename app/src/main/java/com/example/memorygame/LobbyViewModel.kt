package com.example.memorygame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memorygame.BoardDifficulty.EASY
import com.example.memorygame.BoardDifficulty.HARD
import com.example.memorygame.BoardDifficulty.NONE
import com.example.memorygame.BoardDifficulty.NORMAL
import com.example.memorygame.BoardDifficulty.VERY_EASY

class LobbyViewModel : ViewModel() {
    private var _level: MutableLiveData<BoardDifficulty> = MutableLiveData(NONE)
    val level: MutableLiveData<BoardDifficulty>
        get() = _level

    fun onVeryEasyClicked() {
        _level.value = VERY_EASY
    }

    fun onEasyClicked() {
        _level.value = EASY
    }

    fun onNormalClicked() {
        _level.value = NORMAL
    }

    fun onHardClicked() {
        _level.value = HARD
    }
}

enum class BoardDifficulty( val columns: Int, val rows: Int) {
    NONE(0, 0),
    VERY_EASY(5, 2),
    EASY(3, 4),
    NORMAL(4, 4),
    HARD(4, 5)
}