package com.example.memorygame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.memorygame.BoardDifficulty.NONE
import com.example.memorygame.databinding.ActivityLobbyBinding

class LobbyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLobbyBinding
    private lateinit var viewModel: LobbyViewModel

    companion object {
        val EXTRA_DIFICULTY = "DIFFICULTY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LobbyViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lobby)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.level.observe(this, Observer { if (it != NONE) playGame(it) })
    }

    private fun playGame(difficulty: BoardDifficulty) {
        startActivity(
            Intent(this, BoardActivity::class.java)
                .putExtra(EXTRA_DIFICULTY, difficulty)
        )
    }

    fun back(view: View) {}
}
