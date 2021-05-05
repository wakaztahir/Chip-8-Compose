package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberChip8State(
    rows: Int = 4,
    chipsPerRow: Int = 6,
    chipWidth: Float = 100f,
    chipHeight: Float = 40f,
    ballWidth: Float = 40f,
    ballHeight: Float = 40f,
    slateWidth: Float = 220f,
    slateHeight: Float = 30f,
    horizontalSpacing: Int = 32,
    verticalSpacing: Int = 32
) = rememberSaveable(saver = Chip8State.Saver) {
    Chip8State(
        rowsCount = rows,
        chipsPerRow = chipsPerRow,
        chipWidth = chipWidth,
        chipHeight = chipHeight,
        ballWidth = ballWidth,
        ballHeight = ballHeight,
        slateWidth = slateWidth,
        slateHeight = slateHeight,
        horizontalSpacing = horizontalSpacing,
        verticalSpacing = verticalSpacing
    )
}

class Chip8State(
    var rowsCount: Int,
    var chipsPerRow: Int,
    var chipWidth: Float,
    var chipHeight: Float,
    var ballWidth: Float,
    var ballHeight: Float,
    var slateWidth: Float,
    var slateHeight: Float,
    var horizontalSpacing: Int,
    var verticalSpacing: Int,
) {

    var invalidations by mutableStateOf(0)
    var ballMoving by mutableStateOf(true)
    var stepDelay by mutableStateOf(0.toLong())
    var ballSpeed by mutableStateOf(10f)
    val slateX = mutableStateOf(0f)
    val slateY = mutableStateOf(0f)

    val rows = mutableListOf<IntArray>()

    init {
        createCells()
        redraw()
    }

    fun createCells() {
        rows.clear()
        for (rowNumber in 0..rowsCount) {
            val rowList = mutableListOf<Int>()
            for (cellNumber in 0..chipsPerRow) {
                rowList.add(ChipType.Simple)
            }
            rows.add(rowList.toIntArray())
        }
    }

    fun redraw() {
        invalidations++
    }

    companion object {
        val Saver = listSaver<Chip8State, IntArray>(
            save = {
                listOf(
                    //First contains the properties
                    intArrayOf(
                        it.rowsCount,
                        it.chipsPerRow,
                        it.chipWidth.toInt(),
                        it.chipHeight.toInt(),
                        it.ballWidth.toInt(),
                        it.ballHeight.toInt(),
                        it.slateWidth.toInt(),
                        it.slateHeight.toInt(),
                        it.horizontalSpacing,
                        it.verticalSpacing,
                    )
                    //All others contain rows
                    //todo save chips
                )
            },
            restore = {
                Chip8State(
                    rowsCount = it[0][0],
                    chipsPerRow = it[0][1],
                    chipWidth = it[0][2].toFloat(),
                    chipHeight = it[0][3].toFloat(),
                    ballWidth = it[0][4].toFloat(),
                    ballHeight = it[0][5].toFloat(),
                    slateWidth = it[0][6].toFloat(),
                    slateHeight = it[0][7].toFloat(),
                    horizontalSpacing = it[0][8],
                    verticalSpacing = it[0][9]
                )
            }
        )
    }
}