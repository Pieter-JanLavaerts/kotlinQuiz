package com.example.quiz.model

import java.math.BigDecimal

data class AnswerResponse(
    val correct: Boolean,
    val feedback: String,
    val correctAnswersMultipleChoice: List<String>?,
    val correctAnswersRangeMin: BigDecimal?,
    val correctAnswersRangeMax: BigDecimal?,
)
