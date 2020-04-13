package com.example.memorygame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoardViewModel(
    private val difficulty: BoardDifficulty,
    private val boardUseCase: BoardUseCase
) : ViewModel() {
    private val maxScore = (difficulty.columns * difficulty.rows) / 2
    fun getMaxScore() = maxScore

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
          get() = _score

    fun getCardsToPlay() : List<Card> {
        val numberOfCharacters = maxScore
         return boardUseCase.getCardsToPlay(numberOfCharacters)
    }
}
