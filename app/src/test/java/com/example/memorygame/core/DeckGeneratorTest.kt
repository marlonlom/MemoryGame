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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeckGeneratorTest {

    private lateinit var gameController: GameController

    @Before
    fun setUp() {
        gameController = GameController()
    }

    @Test
    fun testSingleCardIsFlipped() {
        gameController.generateNewDeck(4).apply {
            get(0).flip()
            assertFalse(gameController.allCardsAreFaceDown(this))
            assertTrue(get(0).flipped)
        }
    }

    @Test
    fun testTwoCardsAreFlippedButNotPaired() {
        gameController.generateNewDeck(8).apply {
            val firstCard = first { it.card.matches("HORSE") }.apply { flip() }
            val secondCard = last { it.card.matches("HEN") }.apply { flip() }

            assertEquals(2, this.filter { it.card.matches("HORSE") }.size)
            assertEquals(2, this.filter { it.card.matches("HEN") }.size)
            assertTrue(firstCard.flipped)
            assertTrue(secondCard.flipped)
            assertFalse(firstCard.card.matches(secondCard.card.name))
        }
    }

    @Test
    fun testTwoCardsAreFlippedAndPaired() {
        gameController.generateNewDeck(8).apply {
            val cardName = "CAT"
            val firstCard = first { it.card.matches(cardName) }.apply { flip() }
            val secondCard = last { it.card.matches(cardName) }.apply { flip() }

            assertEquals(2, this.filter { it.card.matches(cardName) }.size)
            assertTrue(firstCard.flipped)
            assertTrue(secondCard.flipped)
            assertTrue(firstCard.card.matches(secondCard.card.name))
        }
    }

    @Test
    fun testAllCardsAreFaceDown() {
        gameController.generateNewDeck(4).apply {
            assertTrue(gameController.allCardsAreFaceDown(this))
        }
    }

    @Test
    fun testDeckGeneratedSuccessfully() {
        val deck = gameController.generateNewDeck(4)
        assertTrue(deck.isNotEmpty())
        assertEquals(8, deck.size)
    }

    @Test
    fun testDeckIsNotGeneratedUsingZero() {
        val randomList = GameAttributes.CardItems.randomDeck(0)
        assertTrue(randomList.isNullOrEmpty())
        assertEquals(0, randomList.size)
    }

    @Test
    fun testDeckIsNotGeneratedUsingNegativeNumber() {
        val randomList = GameAttributes.CardItems.randomDeck(-2)
        assertTrue(randomList.isNullOrEmpty())
        assertEquals(0, randomList.size)
    }
}