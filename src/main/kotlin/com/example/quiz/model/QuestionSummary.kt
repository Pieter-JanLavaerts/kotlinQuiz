package com.example.quiz.model

import java.util.UUID

data class QuestionSummary(
    val id: UUID,
    val title: String,
    val type: Question.Type
)
