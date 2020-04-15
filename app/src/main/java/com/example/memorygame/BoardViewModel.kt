package com.example.memorygame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memorygame.State.CLOSE
import com.example.memorygame.State.MATCHED
import com.example.memorygame.State.OPEN


class BoardViewModel(
    private val difficulty: BoardDifficulty,
    private val boardUseCase: BoardUseCase
) : ViewModel() {

    private val INVALID_POSITION = -1
    private val maxScore = (difficulty.columns * difficulty.rows) / 2

    private var isWaitingForMatch = false
    private var firstCardPos = INVALID_POSITION
    private var secondCardPos = INVALID_POSITION

    private var _isBoardActive = MutableLiveData<Boolean>(true)
    val isBoardActive: LiveData<Boolean> get() = _isBoardActive

    private var _cardList = MutableLiveData(boardUseCase.getCardsToPlay(maxScore).toMutableList())
    val cardList: LiveData<MutableList<Card>> get() = _cardList

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    fun getMaxScore() = maxScore

    fun onCardClicked(clickedCard: Card, position: Int) {
        if (clickedCard.state == CLOSE) {
            openCard(clickedCard, position)
            if (!isWaitingForMatch) {
                firstCardPos = position
            } else {
                secondCardPos = position
                lockScreenBy(10000)
                verifyMatch()
            }
            isWaitingForMatch = !isWaitingForMatch
        }
    }

    private fun verifyMatch() {
        val firstCard = cardList.value?.get(firstCardPos)
        val secondCard = cardList.value?.get(secondCardPos)
        val matchResult = boardUseCase.verifyMatch(firstCard, secondCard)
        if (matchResult) {
            val currentScore = score.value ?: 0
            matchCards(firstCard, secondCard)
            _score.value = currentScore + 1
        } else {
            closeCards(firstCard, secondCard)
        }
        firstCardPos = INVALID_POSITION
        secondCardPos = INVALID_POSITION
    }

    private fun lockScreenBy(timeMillis: Long) {
        _isBoardActive.value = false
        Thread.sleep(1000)
        _isBoardActive.value = true
    }

    private fun matchCards(firstCard: Card?, secondCard: Card?) {
        firstCard?.let { matchCard(firstCard, firstCardPos) }
        secondCard?.let { matchCard(secondCard, secondCardPos) }
    }

    private fun closeCards(firstCard: Card?, secondCard: Card?) {
        firstCard?.let { closeCard(firstCard, firstCardPos) }
        secondCard?.let { closeCard(secondCard, secondCardPos) }
    }

    private fun changeCardState(card: Card, position: Int, newState: State) {
        val copyCards = cardList.value ?: mutableListOf()
        copyCards[position] = card.copy(state = newState)
        _cardList.value = copyCards
    }

    private fun openCard(card: Card, position: Int) {
        changeCardState(card, position, OPEN)
    }

    private fun closeCard(card: Card, position: Int) {
        changeCardState(card, position, CLOSE)
    }

    private fun matchCard(card: Card, position: Int) {
        changeCardState(card, position, MATCHED)
    }

    fun areYouWinner(score: Int): Boolean {
        return boardUseCase.areYouWinner(score, difficulty)
    }
}
