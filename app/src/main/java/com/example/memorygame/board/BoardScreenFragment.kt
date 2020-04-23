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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.R
import com.example.memorygame.core.GameAttributes
import com.example.memorygame.core.GameStateContract
import com.example.memorygame.core.GameStateContract.GameResult.FAILED
import com.example.memorygame.core.GameStateContract.GameResult.SUCCESS
import com.example.memorygame.databinding.FragmentBoardBinding
import timber.log.Timber

class BoardScreenFragment : Fragment() {

    private var _viewBinding: FragmentBoardBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel by navGraphViewModels<GameStateContract.ViewModel>(R.id.nav_graph_game)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentBoardBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBoardTable()

        viewModel.gameState
            .observe(viewLifecycleOwner, Observer { state ->
                viewBinding.recyclerBoardTable.apply {
                    (layoutManager as GridLayoutManager).spanCount =
                        state.difficultyLevel.columnCount
                    (adapter as BoardGridAdapter).swapList(state.cardsList)
                }
                checkGameProgress(state)
            })
    }

    private fun checkGameProgress(gameState: GameStateContract.StateItem) {
        when (gameState.gameResult) {
            SUCCESS -> {
                Timber.w("Game finished as SUCCESS (${gameState.score}/${gameState.totalScore}). going to game success screen.")
                findNavController().navigate(BoardScreenFragmentDirections.actionDestBoardToGameSuccessScreenFragment())
            }
            FAILED -> {
                Timber.w("Game finished as FAILED (${gameState.failsCount}/${gameState.difficultyLevel.errorsLimit}). going to game lost screen.")
                findNavController().navigate(BoardScreenFragmentDirections.actionDestBoardToGameFailedScreenFragment())
            }
            else -> {
                checkCurrentStats(gameState)
                if (gameState.cardsToFaceDown.isNotBlank()) {
                    checkCardsToBeFaceDown(gameState)
                } else {
                    Timber.i(
                        requireContext().getString(
                            R.string.text_warning_game_state, gameState.gameResult,
                            gameState.score, gameState.totalScore
                        )
                    )
                }
            }
        }
    }

    private fun checkCurrentStats(gameState: GameStateContract.StateItem) {
        viewBinding.textBoardLabelLevelValue.text = when (gameState.difficultyLevel) {
            GameAttributes.DifficultyLevel.VERY_EASY -> getString(R.string.text_button_difficulty_very_easy)
            GameAttributes.DifficultyLevel.EASY -> getString(R.string.text_button_difficulty_easy)
            GameAttributes.DifficultyLevel.NORMAL -> getString(R.string.text_button_difficulty_normal)
            GameAttributes.DifficultyLevel.HARD -> getString(R.string.text_button_difficulty_hard)
        }
        viewBinding.textBoardLabelScore.text =
            requireContext().getString(R.string.text_board_label_score, gameState.score)
        viewBinding.textBoardLabelFails.apply {
            visibility = if (gameState.difficultyLevel.errorsLimit > 0) View.VISIBLE else View.GONE
            text =
                requireContext().getString(
                    R.string.text_board_label_fails,
                    gameState.failsCount,
                    gameState.difficultyLevel.errorsLimit
                )
        }
    }

    private fun checkCardsToBeFaceDown(gameState: GameStateContract.StateItem) {
        gameState.cardsToFaceDown.split(",").forEach { backedCardId ->
            val foundCard = gameState.cardsList.find { it.uid == backedCardId }
            android.os.Handler().postAtTime({
                Timber.d("Flip down card: $backedCardId")
                (viewBinding.recyclerBoardTable.findViewHolderForAdapterPosition(
                    gameState.cardsList.indexOf(foundCard)
                ) as BoardGridAdapter.ViewHolder).flipBack(foundCard!!)
            }, 400)
        }

    }

    private fun setupBoardTable() {
        viewBinding.recyclerBoardTable.apply {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = BoardGridAdapter().apply {
                setOnCardFlippedListener { cardUniqueId ->
                    handleCardFlipped(cardUniqueId)
                }
            }
        }
    }

    private fun handleCardFlipped(cardUniqueId: String) {
        viewModel.checkFlippedCard(cardUniqueId)
    }

}
