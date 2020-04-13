package com.example.memorygame

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.LobbyActivity.Companion.EXTRA_DIFICULTY
import com.example.memorygame.databinding.ActivityBoardBinding


class BoardActivity : AppCompatActivity(), BoardListener {

    private lateinit var binding: ActivityBoardBinding
    private lateinit var viewModel: BoardViewModel
    private lateinit var difficulty: BoardDifficulty
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        difficulty = intent.getSerializableExtra(EXTRA_DIFICULTY) as BoardDifficulty
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board)
        viewModel = ViewModelProvider(this, BoardViewModelFactory(difficulty, BoardUseCaseImp()))
            .get(BoardViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initViews()
    }

    private fun initViews() {
        binding.boardTableRv.layoutManager = GridLayoutManager (this, difficulty.columns)
        binding.boardTableRv.adapter = BoardAdapter(viewModel.getCardsToPlay(), viewModel.getMaxScore())
    }

    override fun onWinner() {
        binding.boardTableRv.visibility = GONE
        binding.boardLottieAnimation.visibility = VISIBLE
        binding.boardLottieAnimation.playAnimation()
    }

    override fun onMatchEvent() {
        mediaPlayer = MediaPlayer.create(this, R.raw.game_match)
        mediaPlayer.start()
    }

    override fun onWrongMatch() {
        mediaPlayer = MediaPlayer.create(this, R.raw.game_error)
        mediaPlayer.start()
    }
}
