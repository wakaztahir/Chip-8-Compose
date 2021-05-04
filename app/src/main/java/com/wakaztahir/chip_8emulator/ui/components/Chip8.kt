package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun Chip8(
    state: Chip8State = rememberChip8State(),
    backgroundColor: Color = MaterialTheme.colors.onBackground,
    simpleChip: Chip = Chip(type = ChipType.Simple, color = Color.White)
) {

    var rowsNumber = remember(state.invalidations) { 1 } //Current Row Being Drawn , Default : 1
    var curRowChipNumber = remember(state.invalidations) { 1 } //Current Chip Number (in Row) Being Drawn , Default : 1

    Canvas(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
    ) {
        state.invalidations.let { inv ->
            state.rows.forEach {
                curRowChipNumber = 1
                it.forEach { chipType ->
                    when (chipType) {
                        ChipType.Simple -> {
                            drawRoundRect(
                                color = simpleChip.color,
                                topLeft = Offset(
                                    x = (state.horizontalSpacing + (curRowChipNumber * state.chipWidth)),
                                    y = (state.verticalSpacing + (rowsNumber * state.chipHeight))
                                ),
                                size = Size(
                                    width = state.chipWidth,
                                    height = state.chipHeight
                                ),
                                cornerRadius = CornerRadius(
                                    state.chipWidth / 4,
                                    state.chipHeight / 4
                                )
                            )
                        }
                    }
                    curRowChipNumber++
                }
                rowsNumber++
            }
        }
    }

    LaunchedEffect(key1 = null, block = {
        state.redraw()
    })
}