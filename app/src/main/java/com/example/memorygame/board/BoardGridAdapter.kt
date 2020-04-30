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

package com.example.memorygame.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.core.GameAttributes.SingleCard
import com.example.memorygame.databinding.ItemBoardCardBinding
import com.wajahatkarim3.easyflipview.EasyFlipView
import timber.log.Timber

class BoardGridAdapter : RecyclerView.Adapter<BoardGridAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemBoardCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCard(differ.currentList[position], onCardFlippedListener)
    }

    private lateinit var onCardFlippedListener: (String) -> Unit

    private val differ = AsyncListDiffer(this, object : ItemCallback<SingleCard>() {
        override fun areItemsTheSame(oldItem: SingleCard, newItem: SingleCard): Boolean =
            oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: SingleCard, newItem: SingleCard): Boolean =
            oldItem == newItem

    })

    fun swapList(cardsList: List<SingleCard>) {
        differ.submitList(cardsList)
    }

    fun setOnCardFlippedListener(flipListener: (String) -> Unit) {
        onCardFlippedListener = flipListener
    }

    class ViewHolder(private val cardBinding: ItemBoardCardBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {

        fun bindCard(
            cardItem: SingleCard,
            onCardFlippedListener: (String) -> Unit
        ) {
            cardBinding.imageCardFront.setOnClickListener {
                cardBinding.flipper.flipTheView()
            }

            cardBinding.imageCardBack.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        cardBinding.imageCardFront.context,
                        cardItem.card.imageResource
                    )
                )
            }

            Timber.d("flipper.currentFlipState: ${cardBinding.flipper.currentFlipState}")

            cardBinding.flipper.setOnFlipListener { _, newCurrentSide ->
                Timber.d("state: $newCurrentSide, card: ${cardItem.card.name}, flipped before? ${cardItem.flipped}")
                if (EasyFlipView.FlipState.BACK_SIDE == newCurrentSide) {
                    onCardFlippedListener(cardItem.uid)
                }
            }
        }

        fun flipBack(cardItem: SingleCard) {
            Timber.d("flipper.currentFlipState.before: ${cardBinding.flipper.currentFlipState}")
            if (!cardItem.flipped && cardBinding.flipper.isBackSide) {
                cardBinding.flipper.flipTheView()
                Timber.d("flipper.currentFlipState.after: ${cardBinding.flipper.currentFlipState}")
            }
        }
    }
}
