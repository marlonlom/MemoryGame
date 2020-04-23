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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeckPairingLogicTest {

    private lateinit var gameController: GameController

    @Before
    fun setUp() {
        gameController = GameController()
    }

    @Test
    fun testPairingFailedUsingEmptyList() {
        val (list, score, failCount) = gameController.checkPairingCards(
            mutableListOf(),
            "card_7",
            "card_3"
        )
        assertTrue(list.isNullOrEmpty())
        assertEquals(-1, score)
        assertEquals(-1, failCount)
    }

    @Test
    fun testPairingFailedUsingTwoCards() {
        gameController.generateNewDeck(8).apply {
            val animalNames = this.map { it.card.name }.distinct()
            val firstAnimalName = animalNames[0]
            val secondAnimalName = animalNames[1]
            val firstCard = first { it.card.matches(firstAnimalName) }.uid
            val secondCard = first { it.card.matches(secondAnimalName) }.uid

            val (list, score, failCount) = gameController.checkPairingCards(
                this@apply.toMutableList(),
                secondCard,
                firstCard
            )
            assertEquals(2, this.filter { it.card.matches(firstAnimalName) }.size)
            assertEquals(2, this.filter { it.card.matches(secondAnimalName) }.size)

            assertTrue(list.isNotEmpty())
            assertEquals(0, score)
            assertEquals(1, failCount)
        }

    }

    @Test
    fun testPairingSuccessUsingTwoIdenticalCards() {
        gameController.generateNewDeck(8).apply {
            val animalName = random().card.name
            val firstCard = first { it.card.matches(animalName) }.uid
            val secondCard = first { it.card.matches(animalName) }.uid

            val (list, score, failCount) = gameController.checkPairingCards(
                this@apply.toMutableList(),
                secondCard,
                firstCard
            )
            assertEquals(2, this.filter { it.card.matches(animalName) }.size)

            assertTrue(list.isNotEmpty())
            assertEquals(GameAttributes.CardItems.valueOf(animalName).cardScore, score)
            assertEquals(0, failCount)
        }

    }

}