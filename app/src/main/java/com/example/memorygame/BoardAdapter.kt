package com.example.memorygame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class BoardAdapter(var cardsToPlay: List<Card>, val maxScore: Int) :
    RecyclerView.Adapter<BoardAdapter.CardHolder>() {
    val selectedCards  = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardAdapter.CardHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_board_layout, parent, false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return cardsToPlay.size
    }

    override fun onBindViewHolder(holder: BoardAdapter.CardHolder, position: Int) {
        holder.bind(cardsToPlay[position])
    }

    class CardHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var image: ImageView
        fun bind(card: Card) {
            image = view.findViewById(R.id.item_board_card) as ImageView
            if (card.state == State.CLOSE)
                image.setBackgroundResource(R.drawable.ic_card_cover)
            else
                image.setBackgroundResource(card.character.resource)
        }
    }

}
