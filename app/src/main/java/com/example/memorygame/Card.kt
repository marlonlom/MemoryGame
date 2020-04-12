package com.example.memorygame

import com.example.memorygame.State.CLOSE

data class Card(val character: Character, val state: State = CLOSE)

enum class Character(resource: Int) {
    CAT(R.drawable.ic_card_cat),
    COW(R.drawable.ic_card_cow),
    DRAGON(R.drawable.ic_card_dragon),
    MAN(R.drawable.ic_card_garbage_man),
    DOG(R.drawable.ic_card_gosh_dog),
    HEN(R.drawable.ic_card_hen),
    HORSE(R.drawable.ic_card_horse),
    PIG(R.drawable.ic_card_pig),
    SPIDER(R.drawable.ic_card_spider)
}

enum class State {
    CLOSE,
    OPEN,
    MATCHED
}
