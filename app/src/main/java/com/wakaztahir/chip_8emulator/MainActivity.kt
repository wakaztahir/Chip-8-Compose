package com.wakaztahir.chip_8emulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wakaztahir.chip_8emulator.ui.components.Chip8
import com.wakaztahir.chip_8emulator.ui.components.Controls
import com.wakaztahir.chip_8emulator.ui.components.rememberChip8State
import com.wakaztahir.chip_8emulator.ui.theme.Chip8EmulatorTheme

class MainActivity : ComponentActivity() {

    //Game States
    private var gameRunning by mutableStateOf(true);

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chip8EmulatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.onBackground) {
                    AnimatedVisibility(visible = gameRunning) {
                        Column {
                            val chip8State = rememberChip8State()
                            Chip8(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                state = chip8State,
                                onDeath = {
                                    gameRunning = false
                                }
                            )
                            Controls(state = chip8State)
                        }
                    }
                    AnimatedVisibility(visible = !gameRunning) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.chip_8_title),
                                style = MaterialTheme.typography.h5
                            )
                            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                                Button(onClick = {
                                    gameRunning = true
                                }) {
                                    Text(text = stringResource(id = R.string.play))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}