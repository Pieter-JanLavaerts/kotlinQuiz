package com.example.quiz.model

import java.math.BigDecimal
import java.util.UUID

data class AnswerRequest(
    var multipleChoiceAnswers: List<UUID>?,
    var rangeAnswer: BigDecimal?
)
