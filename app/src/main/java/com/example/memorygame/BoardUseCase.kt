package com.example.memorygame

import com.example.memorygame.Character.BAT
import com.example.memorygame.Character.CAT
import com.example.memorygame.Character.COW
import com.example.memorygame.Character.DOG
import com.example.memorygame.Character.DRAGON
import com.example.memorygame.Character.HEN
import com.example.memorygame.Character.HORSE
import com.example.memorygame.Character.MAN
import com.example.memorygame.Character.PIG
import com.example.memorygame.Character.SPIDER

interface BoardUseCase {
    fun getCardsToPlay(numberOfCharacters: Int): List<Card>
    fun verifyMatch(firstCard: Card?, secondCard: Card?): Boolean
    fun areYouWinner(score: Int, difficulty: BoardDifficulty): Boolean
}

class BoardUseCaseImp : BoardUseCase {
    private val cardSet = setOf(
        Card(BAT),
        Card(COW),
        Card(CAT),
        Card(DOG),
        Card(DRAGON),
        Card(HEN),
        Card(HORSE),
        Card(MAN),
        Card(PIG),
        Card(SPIDER)
    )

    private fun getCharacters(numberOfPairs: Int): List<Card> {
        return cardSet.shuffled().subList(0, numberOfPairs)
    }

    override fun getCardsToPlay(numberOfCharacters: Int): List<Card> {
        val characters = getCharacters(numberOfCharacters)
        var cardList = mutableListOf<Card>()
        cardList.addAll(characters.shuffled())
        cardList.addAll(characters.shuffled())
        return cardList
    }

    override fun verifyMatch(firstCard: Card?, secondCard: Card?): Boolean =
        firstCard != null && secondCard != null && (firstCard?.character == secondCard?.character)

    override fun areYouWinner(score: Int, difficulty: BoardDifficulty): Boolean {
        val maxScore = getMaxScore(difficulty)
        return score == maxScore
    }

    private fun getMaxScore(difficulty: BoardDifficulty) =
        (difficulty.columns * difficulty.rows) / 2
}