package com.example.memorygame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.State.CLOSE


class BoardAdapter(private val listener: OnCardItemListener) :
    RecyclerView.Adapter<BoardAdapter.CardHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.character == newItem.character && oldItem.state == newItem.state
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.character == newItem.character && oldItem.state == newItem.state
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_board_layout, parent, false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(differ.currentList[position], listener)
    }

    fun submitCardList(cards: List<Card>) {
        differ.submitList(cards)
    }

    class CardHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.item_board_card) as ImageView

        fun bind(card: Card, listener: OnCardItemListener) {

            if (card.state == CLOSE)
                image.setBackgroundResource(R.drawable.ic_card_cover)
            else
                image.setBackgroundResource(card.character.resource)

            view.setOnClickListener {
                listener.onCardClicked(card, adapterPosition)
            }
        }
    }

    interface OnCardItemListener {
        fun onCardClicked(card: Card, position: Int)
    }
}
