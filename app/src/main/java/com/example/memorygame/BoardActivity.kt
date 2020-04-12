package com.example.memorygame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.memorygame.LobbyActivity.Companion.EXTRA_DIFICULTY
import com.example.memorygame.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding
    private lateinit var viewModel: BoardViewModel
    private lateinit var difficulty: BoardDifficulty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        difficulty = intent.getSerializableExtra(EXTRA_DIFICULTY) as BoardDifficulty
        viewModel = ViewModelProvider(this, BoardViewModelFactory(difficulty, BoardUseCaseImp()))
            .get(BoardViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board)
    }
}
