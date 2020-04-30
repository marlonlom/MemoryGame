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

import com.example.memorygame.core.GameAttributes.SingleCard
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SingleCardDataTest : TestCase() {

    @Test
    fun testSuccessAnimalCardIsFlipped() {
        val singleCard = SingleCard(card = GameAttributes.CardItems.DRAGON)
        singleCard.flip()
        assertTrue(singleCard.flipped)
    }

    @Test
    fun testAnimalCardIsFaceDown() {
        assertFalse(SingleCard(card = GameAttributes.CardItems.BAT).flipped)
    }


    @Test
    fun testRandomListItemsHaveUniqueId() {
        var position = 0
        val randomList = GameAttributes.CardItems.randomDeck(4).map {
            SingleCard(card = it).apply {
                position = position.inc()
                generateUniqueId("card_$position")
            }
        }
        assertTrue(randomList.isNotEmpty())
        assertEquals(8, randomList.size)
        randomList.forEach {
            assertNotNull(it.uid)
        }
    }

}