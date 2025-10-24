package com.example.quiz.model

import java.util.UUID

data class Question(
    val id: UUID,
    val title: String,
    val description: String,
    val feedback: String,
    val type: Type,
    val options: List<QuestionOption>?,
    val range: QuestionRange?,
) {
    enum class Type {
        multiple_choice,
        single_choice,
        range
    }
}
