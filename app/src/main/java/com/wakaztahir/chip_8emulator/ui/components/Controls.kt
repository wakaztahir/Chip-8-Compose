package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wakaztahir.chip_8emulator.R

@Composable
fun Controls(state : Chip8State) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(onClick = {
            state.slateX -= state.slateSpeed
        }) {
            Icon(
                painter = painterResource(id = R.drawable.left_icon),
                contentDescription = stringResource(id = R.string.left_icon)
            )
        }
        IconButton(onClick = {
            state.slateX += state.slateSpeed
        }) {
            Icon(
                painter = painterResource(id = R.drawable.right_icon),
                contentDescription = stringResource(id = R.string.right_icon)
            )
        }
    }
}