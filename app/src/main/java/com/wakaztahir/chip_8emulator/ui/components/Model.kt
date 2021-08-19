package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

enum class ChipType {
    Dead,
    Simple
}

open class Chip(
    val type: ChipType,
    val color: Color,
) {
    var x by mutableStateOf(0f)
    var y by mutableStateOf(0f)
}
