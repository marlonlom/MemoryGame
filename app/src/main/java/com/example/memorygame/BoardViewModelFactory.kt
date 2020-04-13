package com.example.memorygame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BoardViewModelFactory(
    private val difficulty: BoardDifficulty,
    private var boardUseCase: BoardUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoardViewModel(difficulty, boardUseCase) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}

