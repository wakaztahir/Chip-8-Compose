package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun Chip8(
    state: Chip8State = rememberChip8State(),
    backgroundColor: Color = MaterialTheme.colors.onBackground,
    simpleChip: Chip = Chip(type = ChipType.Simple, color = Color.White),
    ballColor: Color = MaterialTheme.colors.primary,
    slateColor: Color = MaterialTheme.colors.secondary,
    onDeath: () -> Unit = {},

    ) {

    val screenWidth = remember { mutableStateOf(0) }
    val screenHeight = remember { mutableStateOf(0) }
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    val increaseX = remember { mutableStateOf(false) }
    val increaseY = remember { mutableStateOf(false) }

    val ballX = remember(screenWidth.value) {
        mutableStateOf(((screenWidth.value / 2) - (state.ballWidth / 2)))
    }
    val ballY = remember(screenHeight.value) {
        mutableStateOf((screenHeight.value - (state.slateHeight * 2) - (state.ballHeight)))
    }
    Canvas(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
            .onGloballyPositioned {
                screenWidth.value = it.size.width
                screenHeight.value = it.size.height
                state.slateX.value = it.size.width / 2 - (state.slateWidth / 2)
                state.slateY.value = (it.size.height - (state.slateHeight * 2))
            }
    ) {
        //Drawing the chips
        state.invalidations.let { inv ->
            state.rows.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { chipIndex, chipType ->
                    val chipRowIndex = (rowIndex + 1) / (chipIndex + 1)
                    val chipXStart =
                        offsetX.value + (chipRowIndex * state.chipWidth) + state.horizontalSpacing
                    val chipYStart =
                        offsetY.value + ((rowIndex + 1) * state.chipHeight) + state.verticalSpacing

                    when (chipType) {
                        ChipType.Simple -> {
                            drawRoundRect(
                                color = simpleChip.color,
                                topLeft = Offset(
//                                    offsetX.value + (((curRowChipNumber - 1) * state.horizontalSpacing / 2) + (curRowChipNumber * state.chipWidth))
                                    x = chipXStart,
//                                    offsetY.value + (((currentRowNumber - 1) * state.verticalSpacing / 2) + (currentRowNumber * state.chipHeight))
                                    y = chipYStart
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
                }
            }
        }

        //Drawing the ball
        drawRoundRect(
            topLeft = Offset(
                x = ballX.value,
                y = ballY.value
            ),
            size = Size(
                width = state.ballWidth,
                height = state.ballHeight
            ),
            color = ballColor,
            cornerRadius = CornerRadius(x = state.ballWidth / 2, y = state.ballHeight / 2)
        )
        //Drawing the slate
        drawRoundRect(
            topLeft = Offset(
                x = state.slateX.value,
                y = state.slateY.value
            ),
            size = Size(
                width = state.slateWidth,
                height = state.slateHeight,
            ),
            color = slateColor,
            cornerRadius = CornerRadius(x = (state.slateWidth / 4), y = (state.slateHeight / 4))
        )
    }

    //Calculates chips per row , x-offset to center chips
    LaunchedEffect(screenWidth.value, screenHeight.value) {
        if (screenWidth.value != 0) {
            val chipsPerRow =
                screenWidth.value / (state.chipWidth + ((state.chipsPerRow - 1) * state.horizontalSpacing))
            offsetX.value =
                (screenWidth.value - ((chipsPerRow) * state.chipWidth + ((chipsPerRow + 1) * state.horizontalSpacing * 2))) / 2
            offsetY.value = 32f
            state.chipsPerRow = chipsPerRow.roundToInt()
            state.createCells()
        }
        state.redraw()
    }


    //Moves the ball , detects collision
    LaunchedEffect(screenWidth.value, screenHeight.value, ballX.value, ballY.value) {
        var newX = ballX.value
        var newY = ballY.value

        if (state.ballMoving) {
            if (increaseX.value) {
                newX += state.ballSpeed
            } else {
                newX -= state.ballSpeed
            }
            if (increaseY.value) {
                newY += state.ballSpeed
            } else {
                newY -= state.ballSpeed
            }
        }

        //Chip Collision Detection
        state.rows.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { chipIndex, chipType ->
//                try {
                val chipRowIndex = (rowIndex + 1) / (chipIndex + 1)
                val chipXStart =
                    offsetX.value + (chipRowIndex * state.chipWidth) + state.horizontalSpacing
                val chipXEnd = chipXStart + state.chipWidth
                val chipYStart =
                    offsetY.value + ((rowIndex + 1) * state.chipHeight) + state.verticalSpacing
                val chipYEnd = chipYStart + state.chipHeight
                if (
                    chipType != ChipType.Dead &&
                    newX > (chipXStart) && newX < (chipXEnd) &&
                    newY > (chipYStart) && newY < (chipYEnd)
                ) {
                    row[chipIndex] = ChipType.Dead
                    increaseX.value = !increaseX.value
                    increaseY.value = !increaseY.value
                    state.redraw()
                }
//                } catch (ex: Exception) {
//                    Log.d("Chip8Log", "YOError", ex)
//                }
            }
        }


        //Screen Collision Detection
        if (newX < 0) {
            increaseX.value = true
        } else if (newX > (screenWidth.value - state.ballWidth)) {
            increaseX.value = false
        }
        if (newY < 0) {
            increaseY.value = true
        } else if (
        //slate collision detection
            (newX + state.ballWidth) > (state.slateX.value) && newX < (state.slateX.value + state.slateWidth) &&
            (newY + state.ballHeight) > (state.slateY.value) && newY < (state.slateY.value + state.slateHeight)
        ) {
            increaseY.value = false
        } else if (newY > (state.slateY.value + state.slateHeight)) {
            //Lost the game
            increaseY.value = false //todo remove this
            onDeath()
        }



        delay(state.stepDelay)
        ballX.value = newX
        ballY.value = newY
    }
}