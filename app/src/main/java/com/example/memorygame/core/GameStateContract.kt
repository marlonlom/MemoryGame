/*
 * Copyright 2020 aliceresponde. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.example.memorygame.core

import androidx.lifecycle.MutableLiveData

interface GameStateContract {

    enum class GameResult {
        SUCCESS, FAILED, PLAYING
    }

    data class StateItem(
        val difficultyLevel: GameAttributes.DifficultyLevel,
        private var _score: Int = 0,
        private var _failsCount: Int = 0,
        private var _cardWaitingPair: String = "",
        private var _faceDownCardIds: String = "",
        val cardsList: List<GameAttributes.SingleCard>
    ) {
        val score get() = _score
        val failsCount get() = _failsCount
        val pairingCardId get() = _cardWaitingPair
        val cardsToFaceDown get() = _faceDownCardIds
        val totalScore get() = cardsList.distinctBy { it.card.name }.sumBy { it.card.cardScore }

        private val gameSuccess: Boolean
            get() = _score >= totalScore

        private val gameFailed
            get() = difficultyLevel.errorsLimit in 1.._failsCount

        val gameResult
            get() = if (gameSuccess) GameResult.SUCCESS else if (gameFailed) GameResult.FAILED else GameResult.PLAYING

        fun computeScore(obtainedScore: Int) {
            _score = _score.plus(obtainedScore)
        }

        fun changePairingCard(cardUid: String) {
            _cardWaitingPair = cardUid
        }

        fun computeFails(failCount: Int) {
            _failsCount = _failsCount.plus(failCount)
        }

        fun changeCardIdsToFaceDown(faceDownCardIds: String) {
            _faceDownCardIds = faceDownCardIds
        }
    }

    class ViewModel : androidx.lifecycle.ViewModel() {

        private val _gameState = MutableLiveData<StateItem>()
        private val gameController = GameController()

        val gameState get() = _gameState

        fun createNewGame(difficultyLevel: GameAttributes.DifficultyLevel) {
            _gameState.value = StateItem(
                difficultyLevel,
                0,
                0,
                "",
                "",
                gameController.generateNewDeck(difficultyLevel.rowCount)
            )
        }

        fun checkFlippedCard(cardUniqueId: String) {
            val actualState = _gameState.value!!
            var newStateItem: StateItem? = null

            if (gameController.allCardsAreFaceDown(actualState.cardsList) || actualState.pairingCardId.isBlank()) {
                newStateItem =
                    actualState.copy(
                        _cardWaitingPair = cardUniqueId,
                        cardsList = actualState.cardsList.toMutableList().map {
                            if (cardUniqueId == it.uid && !it.flipped) {
                                it.flip()
                            }
                            it
                        }.toList()
                    )
            } else if (actualState.pairingCardId.isNotBlank()) {
                val (newCardsList, gainedScore, failsCount) = gameController.checkPairingCards(
                    actualState.cardsList.toMutableList(),
                    cardUniqueId,
                    actualState.pairingCardId
                )
                val faceDownCardIds =
                    if (failsCount > 0) "${actualState.pairingCardId},$cardUniqueId" else ""

                newStateItem =
                    actualState.copy(
                        _cardWaitingPair = cardUniqueId,
                        cardsList = newCardsList
                    ).apply {
                        changePairingCard("")
                        changeCardIdsToFaceDown(faceDownCardIds)
                        computeScore(gainedScore)
                        computeFails(failsCount)
                    }
            }

            _gameState.value = newStateItem
        }

    }
}
