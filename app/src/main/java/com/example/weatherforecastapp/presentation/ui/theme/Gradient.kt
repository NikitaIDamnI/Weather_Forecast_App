package com.example.weatherforecastapp.presentation.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Gradient(
    val primaryGradient: Brush,
    val secondaryGradient: Brush,
    val shadowColor: Color
) {
    constructor(
        firstColor: Color,
        secondColor: Color,
        thirdColor: Color,
        fourthColor: Color
    ) : this(
        primaryGradient = Brush.verticalGradient(colors = listOf(firstColor, secondColor)),
        secondaryGradient = Brush.verticalGradient(colors = listOf(thirdColor, fourthColor)),
        shadowColor = Color.Black
    )
}

object Gradients {
    val cardGradients = listOf(
        Gradient(
            firstColor = Color(0xFFFFF176),
            secondColor = Color(0xFFFF5621),
            thirdColor = Color(0xFFE7A984),
            fourthColor = Color(0xFFFD6A29),
        ),
        Gradient(
            firstColor = Color(0xFFBA68C8),
            secondColor = Color(0xFF7986CB),
            thirdColor = Color(0xFF7E57C2),
            fourthColor = Color(0xff7f83cb),
        ),
        Gradient(
            firstColor = Color(0xFFAED581),
            secondColor = Color(0xFF25FFFF),
            thirdColor = Color(0xFF66BB6A),
            fourthColor = Color(0xDC048813),
        ),
        Gradient(
            firstColor = Color(0xFF379FFF),
            secondColor = Color(0xFF4021FF),
            thirdColor = Color(0xFF987BCA),
            fourthColor = Color(0x523895FF),
        ),
        Gradient(
            firstColor = Color(0xFFFF7AA2),
            secondColor = Color(0xFF9021FF),
            thirdColor = Color(0xFFB33DE2),
            fourthColor = Color(0x00F270AD),
        ),
    )

    val temperature = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0D47A1), // Темно-синий (очень холодно)
            Color(0xFF1976D2), // Голубой (прохладный)
            Color(0xFF42A5F5), // Светло-голубой (немного прохладный)
            Color(0xFF00E676), // Зеленый (комфортная температура)
            Color(0xFFFFEB3B), // Желтый (тепло)
            Color(0xFFFF5722), // Красный (очень жарко)
            Color(0xFFE40606)  // Красный (очень жарко)
        )
    )
}