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

import com.example.memorygame.R


interface GameAttributes {

    enum class DifficultyLevel(val columnCount: Int, val rowCount: Int, val errorsLimit: Int) {
        VERY_EASY(3, 2, -1),
        EASY(3, 4, 3),
        NORMAL(4, 6, 3),
        HARD(4, 8, 1);
    }

    data class SingleCard(
        private var _uid: String = "",
        val card: CardItems,
        private var _cardFlipped: Boolean = false
    ) {

        val flipped
            get() = _cardFlipped

        fun flip() {
            _cardFlipped = !_cardFlipped
        }

        val uid
            get() = _uid

        fun generateUniqueId(uuid: String) {
            _uid = uuid
        }
    }

    enum class CardItems(
        private val _resource: Int,
        private val _cardScore: Int
    ) {
        BAT(R.drawable.ic_card_bat, 4),
        CAT(R.drawable.ic_card_cat, 2),
        DRAGON(R.drawable.ic_card_dragon, 10),
        DOG(R.drawable.ic_card_gosh_dog, 2),
        COW(R.drawable.ic_card_cow, 8),
        HEN(R.drawable.ic_card_hen, 6),
        HORSE(R.drawable.ic_card_horse, 8),
        MAN(R.drawable.ic_card_garbage_man, 12),
        PIG(R.drawable.ic_card_pig, 4),
        SPIDER(R.drawable.ic_card_spider, 6);

        val cardScore
            get() = _cardScore

        val imageResource
            get() = _resource


        fun matches(anotherAnimalName: String): Boolean = this == valueOf(anotherAnimalName)

        companion object {

            @JvmStatic
            fun getTotalPairedCardsScore(cardItems: List<CardItems>): Int =
                cardItems.distinct().map { it.cardScore }.reduce { acc, score -> acc + score }

            @JvmStatic
            fun randomDeck(rowCount: Int): List<CardItems> =
                mutableListOf(values()).random().slice(IntRange(0, rowCount - 1))
                    .duplicated()
                    .shuffled()
        }

    }
}

fun <E> Collection<E>.duplicated(): List<E> = this.plus(this)
