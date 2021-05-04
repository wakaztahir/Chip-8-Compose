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
    cellsPerRow: Int = 10,
    chipWidth: Float = 100f,
    chipHeight: Float = 40f,
    horizontalSpacing: Int = 16,
    verticalSpacing: Int = 16
) = rememberSaveable(saver = Chip8State.Saver) {
    Chip8State(
        rowsCount = rows,
        cellsPerRow = cellsPerRow,
        chipWidth = chipWidth,
        chipHeight = chipHeight,
        horizontalSpacing = horizontalSpacing,
        verticalSpacing = verticalSpacing
    )
}

class Chip8State(
    val rowsCount: Int,
    val cellsPerRow: Int,
    val chipWidth: Float,
    val chipHeight: Float,
    val horizontalSpacing: Int,
    val verticalSpacing: Int,
) {

    var invalidations by mutableStateOf(0)

    val rows = mutableListOf<IntArray>()

    init {
        for (rowNumber in 0..rowsCount) {
            val rowList = mutableListOf<Int>()
            for (cellNumber in 0..cellsPerRow) {
                rowList.add(ChipType.Simple)
            }
            rows.add(rowList.toIntArray())
        }
        redraw()
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
                        it.cellsPerRow,
                        it.chipWidth.toInt(),
                        it.chipHeight.toInt(),
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
                    cellsPerRow = it[0][1],
                    chipWidth = it[0][2].toFloat(),
                    chipHeight = it[0][3].toFloat(),
                    horizontalSpacing = it[1][3],
                    verticalSpacing = it[1][4]
                )
            }
        )
    }
}