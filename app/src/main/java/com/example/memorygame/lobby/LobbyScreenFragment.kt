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

package com.example.memorygame.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.memorygame.R
import com.example.memorygame.core.GameAttributes
import com.example.memorygame.core.GameStateContract
import com.example.memorygame.databinding.FragmentLobbyBinding
import com.example.memorygame.databinding.LayoutBottomDifficultySelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class LobbyScreenFragment : Fragment() {

    private var _viewBinding: FragmentLobbyBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel by navGraphViewModels<GameStateContract.ViewModel>(R.id.nav_graph_game)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentLobbyBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.buttonNewGame.setOnClickListener {
            BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            ).apply {
                val difficultySelectionViewBinding =
                    LayoutBottomDifficultySelectionBinding.inflate(this.layoutInflater)
                difficultySelectionViewBinding.buttonLevelVeryEasy.setOnClickListener {
                    onDifficultyLevelSelected(this, GameAttributes.DifficultyLevel.VERY_EASY)
                }
                difficultySelectionViewBinding.buttonLevelEasy.setOnClickListener {
                    onDifficultyLevelSelected(this, GameAttributes.DifficultyLevel.EASY)
                }
                difficultySelectionViewBinding.buttonLevelNormal.setOnClickListener {
                    onDifficultyLevelSelected(this, GameAttributes.DifficultyLevel.NORMAL)
                }
                difficultySelectionViewBinding.buttonLevelHard.setOnClickListener {
                    onDifficultyLevelSelected(this, GameAttributes.DifficultyLevel.HARD)
                }
                this.setContentView(difficultySelectionViewBinding.root)
                this.show()
            }
        }
    }

    private fun onDifficultyLevelSelected(
        dialog: BottomSheetDialog,
        difficultyLevel: GameAttributes.DifficultyLevel
    ) {
        viewModel.createNewGame(difficultyLevel)
        findNavController().navigate(LobbyScreenFragmentDirections.actionGlobalDestBoard())
        dialog.dismiss()
    }
}