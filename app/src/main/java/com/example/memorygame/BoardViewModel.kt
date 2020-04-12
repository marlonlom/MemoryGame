package com.example.memorygame

import androidx.lifecycle.ViewModel

class BoardViewModel(
    private val difficulty: BoardDifficulty,
    private var boardUseCase: BoardUseCase
) : ViewModel() {

    fun getGameCards() {
        boardUseCase.getCardsToPlay( getCardByDifficulty(difficulty))
    }

    private fun getCardByDifficulty(difficulty: BoardDifficulty): Int {
        return boardUseCase.countBoardCards(difficulty)
    }
}
