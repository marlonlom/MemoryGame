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
import com.example.memorygame.core.GameAttributes.CardItems.Companion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class CardItemsDataTest {

    @Test
    fun testSuccessAnimalCardMatches() {
        val animalName = "HEN"
        assertTrue(CardItems.valueOf(animalName).matches(animalName))
    }

    @Test
    fun testFailAnimalCardMatches() {
        val animalName = "HEN"
        assertFalse(
            CardItems.valueOf(animalName).matches(CardItems.CAT.name)
        )
    }

    @Test
    fun testSuccessRandomListTotalScoreComputation() {
        val animalNames = listOf(
            CardItems.HEN,
            CardItems.COW,
            CardItems.HORSE
        )

        val randomList = CardItems.randomDeckFromNames(
            animalNames.map { it.name }
        )

        val totalPairedCardsScore =
            CardItems.getTotalPairedCardsScore(randomList)

        val expectedTotalPairedCardsScore = animalNames.map { it.cardScore }.sum()

        assertSame(expectedTotalPairedCardsScore, totalPairedCardsScore)
    }

    @Test
    fun testFailRandomListTotalScoreComputation() {
        val animalNames = listOf(
            CardItems.HEN,
            CardItems.COW,
            CardItems.HORSE
        )

        val totalPairedCardsScore = CardItems.getTotalPairedCardsScore(
            CardItems.randomDeckFromNames(animalNames.map { it.name })
        )

        val expectedTotalPairedCardsScore = listOf(
            CardItems.DOG,
            CardItems.SPIDER
        ).map { it.cardScore }.sum()

        assertNotSame(expectedTotalPairedCardsScore, totalPairedCardsScore)
    }

    @Test
    fun testSuccessRandomListGeneration() {
        val randomList = CardItems.randomDeck(4)
        assertTrue(randomList.isNotEmpty())
        assertEquals(8, randomList.size)
    }

    @Test
    fun testErrorRandomListGenerationWithZero() {
        val randomList = CardItems.randomDeck(0)
        assertTrue(randomList.isNullOrEmpty())
        assertEquals(0, randomList.size)
    }

    @Test
    fun testErrorRandomListGenerationWithNegativeNumber() {
        val randomList = CardItems.randomDeck(-2)
        assertTrue(randomList.isNullOrEmpty())
        assertEquals(0, randomList.size)
    }

}

private fun Companion.randomDeckFromNames(animalNames: List<String>): List<CardItems> =
    mutableListOf(animalNames.map { animalName -> CardItems.valueOf(animalName) }).random()
        .slice(IntRange(0, animalNames.size - 1)).duplicated()
        .shuffled()
