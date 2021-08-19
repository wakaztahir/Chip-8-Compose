package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

class Chip8State(
    val chipsPerRow: Int = 6,
    val rowsCount: Int = 7,
    val chipsColor: Color = Color.Black,
    val randomColors: Boolean = true,
    val ballSize: Float = 30f,
    val slateWidth: Float = 240f,
    val slateHeight: Float = 30f,
    val slateSpeed : Float = 80f,
    val horizontalOffset: Float = 30f,
    val verticalOffset: Float = 30f,
    val chipXPadding: Float = 10f,
    val chipYPadding: Float = 10f,

    val colors: List<Color> = listOf(
        Color.Blue,
        Color.Red,
        Color.Yellow,
        Color.Magenta,
        Color.DarkGray,
        Color.Cyan,
        Color.Green,
    )

) {

    var chipWidth by mutableStateOf(60f)
        internal set

    var chipHeight by mutableStateOf(40f)

    var invalidations by mutableStateOf(0)
    var ballMoving by mutableStateOf(true)
    var stepDelay by mutableStateOf(0.toLong())
    var ballSpeed by mutableStateOf(10)
    var slateX by mutableStateOf(0f)
    var slateY by mutableStateOf(0f)

    val chips = mutableStateListOf<Chip>()

    fun createCells(screenWidth: Float) {
        chipWidth =
            (screenWidth - (horizontalOffset * 2) - (chipXPadding * chipsPerRow)) / chipsPerRow
        val totalChips = chipsPerRow * rowsCount
        var chipX = horizontalOffset
        var chipY = verticalOffset
        for (chipNo in 1..totalChips) {
            chips.add(
                Chip(
                    ChipType.Simple,
                    color = if (!randomColors) chipsColor else colors.random()
                ).apply {
                    x = chipX
                    y = chipY
                }
            )
            chipX += chipXPadding + chipWidth
            if (chipNo % chipsPerRow == 0) {
                chipX = horizontalOffset
                chipY += chipHeight + chipYPadding
            }
        }
    }

    fun redraw() {
        invalidations++
    }
}

@Composable
fun rememberChip8State(color: Color = Color.Black) = remember {
    Chip8State(
        chipsColor = color
    )
}