package com.wakaztahir.chip_8emulator.ui.components

import androidx.compose.ui.graphics.Color

object ChipType {
    const val Dead = 0
    const val Simple = 1
}

open class Chip(
    val type: Int,
    val color:Color,
    )
