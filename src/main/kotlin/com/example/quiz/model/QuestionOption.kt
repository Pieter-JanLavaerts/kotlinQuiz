package com.example.quiz.model

import java.util.UUID

data class QuestionOption(
    val id: UUID,
    val text: String,
    val correct: Boolean,
)
