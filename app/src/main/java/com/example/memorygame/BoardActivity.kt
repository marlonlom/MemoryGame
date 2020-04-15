package com.example.memorygame

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.LobbyActivity.Companion.EXTRA_DIFICULTY
import com.example.memorygame.databinding.ActivityBoardBinding


class BoardActivity : AppCompatActivity(), BoardAdapter.OnCardItemListener {

    private lateinit var boardAdapter: BoardAdapter
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
        initObservers()
    }

    private fun initViews() {
        binding.boardTableRv.apply {
            layoutManager = GridLayoutManager(this@BoardActivity, difficulty.columns)
            boardAdapter = BoardAdapter( this@BoardActivity)
            adapter = boardAdapter
        }
        binding.boardBackBtn.setOnClickListener { onBackPressed() }
    }

    private fun initObservers() {
        viewModel.score.observe(this, Observer { onScoreChanged(it) })
        viewModel.cardList.observe(this, Observer { updateAdapter(it) })
    }

    private fun updateAdapter(cardList: MutableList<Card>) {
        boardAdapter.submitCardList(cardList)
        boardAdapter.notifyDataSetChanged()
    }

    private fun onScoreChanged(score: Int) {
        val isWinner: Boolean = viewModel.areYouWinner(score)
        if (isWinner)
            playWinnerAnimation()
        else
            playMatch()
    }

    private fun playMatch() {
        mediaPlayer = MediaPlayer.create(this, R.raw.game_match)
        mediaPlayer.start()
    }

    private fun playMatchError() {
        mediaPlayer = MediaPlayer.create(this, R.raw.game_error)
        mediaPlayer.start()
    }

    private fun playWinnerAnimation() {
        binding.boardTableRv.visibility = GONE
        binding.boardLottieAnimation.visibility = VISIBLE
        binding.boardLottieAnimation.playAnimation()
    }

    override fun onCardClicked(card: Card, pos: Int) = viewModel.onCardClicked(card, pos)
}

