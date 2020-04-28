package example.com.furnitureproject.utils

import android.graphics.Color

class ColorUtils {
    companion object {
        private val DEFAULT_COLOR = Color.parseColor("#DFDFDF")
        private val DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD")
        private val COLOR_BLUE = Color.parseColor("#33B5E5")
        private val COLOR_VIOLET = Color.parseColor("#AA66CC")
        private val COLOR_GREEN = Color.parseColor("#99CC00")
        private val COLOR_ORANGE = Color.parseColor("#FFBB33")
        private val COLOR_RED = Color.parseColor("#FF4444")
        private val COLOR_PURPLE = Color.parseColor("#cc00cc")
        private val COLOR_CYAN = Color.parseColor("#00ffff")
        private val COLOR_YELLOW = Color.parseColor("#ffff66")
        private val COLORS = intArrayOf(COLOR_BLUE, COLOR_VIOLET, COLOR_GREEN, COLOR_ORANGE, COLOR_RED, COLOR_PURPLE, COLOR_CYAN, COLOR_YELLOW)
        private val DARKEN_SATURATION = 1.1f
        private val DARKEN_INTENSITY = 0.9f
        private var COLOR_INDEX = 0

        fun pickColor(): Int {
            return COLORS[Math.round(Math.random() * (COLORS.size - 1)).toInt()]
        }

        fun nextColor(): Int {
            if (COLOR_INDEX >= COLORS.size) {
                COLOR_INDEX = 0
            }
            return COLORS[COLOR_INDEX++]
        }

        fun resetIndex(){
            COLOR_INDEX = 0
        }
    }


}