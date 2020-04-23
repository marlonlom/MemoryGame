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

import com.example.memorygame.core.GameAttributes.CardItems
import com.example.memorygame.core.GameAttributes.SingleCard
import timber.log.Timber

class GameController {

    fun generateNewDeck(rowCount: Int): List<SingleCard> {
        var position = 0
        return CardItems.randomDeck(rowCount).map {
            SingleCard(card = it).apply {
                position = position.inc()
                generateUniqueId("card_$position")
            }
        }
    }

    fun allCardsAreFaceDown(cardsList: List<SingleCard>): Boolean =
        cardsList.filter { it.flipped }.isNullOrEmpty()

    fun checkPairingCards(
        actualList: MutableList<SingleCard>,
        secondCardId: String,
        pairingCardId: String
    ): Triple<List<SingleCard>, Int, Int> {
        if (actualList.isNullOrEmpty()) {
            return Triple(actualList, -1, -1)
        }
        val pairingCard = actualList.find { it.uid == pairingCardId }
        val selectedCard = actualList.find { it.uid == secondCardId }
        Timber.d("checkPairingCards(${pairingCard!!.card.name},${selectedCard!!.card.name})")
        val matchesCardNames = pairingCard.card.matches(selectedCard.card.name)
        Timber.d("matchesCardNames? $matchesCardNames")
        val pairingFailCount = if (!matchesCardNames) 1 else 0
        Timber.d("pairingFailCount: $pairingFailCount")
        val gainedScore = if (matchesCardNames) selectedCard.card.cardScore else 0
        Timber.d("gainedScore: $gainedScore")

        val updatedList = updateActualListWithMatches(
            actualList,
            matchesCardNames,
            pairingCardId,
            secondCardId
        )

        return Triple(updatedList, gainedScore, pairingFailCount)
    }

    private fun updateActualListWithMatches(
        actualList: MutableList<SingleCard>,
        matchesCardNames: Boolean,
        pairingCardId: String,
        secondCardId: String
    ): List<SingleCard> =
        actualList.map {
            if (matchesCardNames) {
                if (it.uid == pairingCardId && !it.flipped) {
                    it.flip()
                } else if (it.uid == secondCardId && !it.flipped) {
                    it.flip()
                }
            } else {
                if (it.uid == pairingCardId && it.flipped) {
                    it.flip()
                } else if (it.uid == secondCardId && it.flipped) {
                    it.flip()
                }
            }
            it
        }.toList()
}
