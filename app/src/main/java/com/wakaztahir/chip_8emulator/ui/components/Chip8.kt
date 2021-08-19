package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.delay

@Composable
fun Chip8(
    modifier : Modifier = Modifier,
    state: Chip8State = rememberChip8State(),
    backgroundColor: Color = MaterialTheme.colors.onBackground,
    ballColor: Color = MaterialTheme.colors.primary,
    slateColor: Color = MaterialTheme.colors.secondary,
    onHitChip: (Chip) -> Unit = {},
    onDeath: () -> Unit = {},
) {

    var screenWidth by remember { mutableStateOf(0) }
    var screenHeight by remember { mutableStateOf(0) }
    var increaseX by remember { mutableStateOf(false) }
    var increaseY by remember { mutableStateOf(true) }

    val ballX = remember(screenWidth) {
        mutableStateOf(((screenWidth / 2) - (state.ballSize / 2)))
    }
    val ballY = remember(screenHeight) {
        mutableStateOf((screenHeight - (state.slateHeight * 2) - (state.ballSize)))
    }

    Canvas(
        modifier = modifier
            .background(backgroundColor)
            .onGloballyPositioned {
                screenWidth = it.size.width
                screenHeight = it.size.height
            }
    ) {
        //Drawing the chips
        state.invalidations.let { inv ->
            state.chips.forEachIndexed { rowIndex, chip ->
                drawRoundRect(
                    color = chip.color,
                    topLeft = Offset(
                        x = chip.x,
                        y = chip.y
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

            //Drawing the ball
            drawRoundRect(
                topLeft = Offset(
                    x = ballX.value,
                    y = ballY.value
                ),
                size = Size(
                    width = state.ballSize,
                    height = state.ballSize
                ),
                color = ballColor,
                cornerRadius = CornerRadius(x = state.ballSize / 2, y = state.ballSize / 2)
            )

            //Drawing the slate
            drawRoundRect(
                topLeft = Offset(
                    x = state.slateX,
                    y = state.slateY
                ),
                size = Size(
                    width = state.slateWidth,
                    height = state.slateHeight,
                ),
                color = slateColor,
                cornerRadius = CornerRadius(x = (state.slateWidth / 4), y = (state.slateHeight / 4))
            )
        }
    }

    // Create Chips , Center Slate
    LaunchedEffect(screenWidth, screenHeight) {
        if (screenWidth != 0) {
            //Creating Chips
            state.createCells(screenWidth = screenWidth.toFloat())
            //Centering Slate
            state.slateX = screenWidth / 2 - (state.slateWidth / 2)
            state.slateY = (screenHeight - (state.slateHeight * 2))
        }
        state.redraw()
    }


    //Moves the ball , detects collision
    LaunchedEffect(screenWidth, screenHeight, ballX.value, ballY.value) {

        // Ball Movement
        var newX = ballX.value
        var newY = ballY.value
        if (state.ballMoving) {
            if (increaseX) {
                newX += state.ballSpeed
            } else {
                newX -= state.ballSpeed
            }
            if (increaseY) {
                newY += state.ballSpeed
            } else {
                newY -= state.ballSpeed
            }
        }

        // Chip Collision Detection
        val chipIterator = state.chips.iterator()
        while (chipIterator.hasNext()) {
            val chip = chipIterator.next()
            if (newX > chip.x && newX < chip.x + state.chipWidth && newY > chip.y && newY < chip.y + state.chipHeight) {
                // Hit a chip
                chipIterator.remove()
                onHitChip(chip)
                break;
            }
        }

        //Screen Collision Detection
        if (newX < 0) {
            increaseX = true
        } else if (newX > (screenWidth - state.ballSize)) {
            increaseX = false
        }
        if (newY < 0) {
            increaseY = true
        } else if (
        //Slate collision detection
            (newX + state.ballSize) > (state.slateX) && newX < (state.slateX + state.slateWidth) &&
            (newY + state.ballSize) > (state.slateY) && newY < (state.slateY + state.slateHeight)
        ) {
            increaseY = false
        } else if (newY > (state.slateY + state.slateHeight)) {
            //Lost the game
            increaseY = false //todo remove this
            onDeath()
        }

        delay(state.stepDelay)
        ballX.value = newX
        ballY.value = newY
    }
}