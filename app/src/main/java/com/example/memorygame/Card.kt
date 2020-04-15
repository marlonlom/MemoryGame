package com.example.memorygame

import com.example.memorygame.State.CLOSE

data class Card(val character: Character, var state: State = CLOSE)

enum class Character(val resource: Int) {
    BAT(R.drawable.ic_card_bat),
    CAT(R.drawable.ic_card_cat),
    DRAGON(R.drawable.ic_card_dragon),
    DOG(R.drawable.ic_card_gosh_dog),
    COW(R.drawable.ic_card_cow),
    HEN(R.drawable.ic_card_hen),
    HORSE(R.drawable.ic_card_horse),
    MAN(R.drawable.ic_card_garbage_man),
    PIG(R.drawable.ic_card_pig),
    SPIDER(R.drawable.ic_card_spider)
}

enum class State {
    CLOSE,
    OPEN,
    MATCHED
}
