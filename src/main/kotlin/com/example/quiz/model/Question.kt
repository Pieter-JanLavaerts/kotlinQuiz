package com.example.quiz.model

import java.util.UUID

data class Question private constructor(
    val id: UUID?,
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
    companion object {
        fun create(
            id: UUID?,
            title: String,
            description: String,
            feedback: String,
            type: Type,
            options: List<QuestionOption>?,
            range: QuestionRange?
        ): Question {
            require(title.isNotBlank()) { "Title must not be blank" }
            require(description.isNotBlank()) { "Description must not be blank" }

            when (type) {
                Type.range -> {
                    require(range != null) { "Range question must have a range" }
                    require(options.isNullOrEmpty()) { "Range question cannot have options" }
                }
                Type.single_choice, Type.multiple_choice -> {
                    require(!options.isNullOrEmpty()) { "Choice question must have options" }
                    if (type == Type.single_choice) {
                        require(options.filter { it.correct }.size == 1) { "Single choice questions have a single answer"}
                    }
                    require(range == null) { "Choice question cannot have range" }
                }
            }

            return Question(id, title, description, feedback, type, options, range)
        }
    }

    fun evaluateAnswer(answer: AnswerRequest): AnswerResponse {
        val isCorrect = when (type) {
            Type.range -> {
                val rangeAnswer = answer.rangeAnswer
                rangeAnswer != null && rangeAnswer in range!!.min..range.max
            }
            Type.single_choice, Type.multiple_choice -> {
                val answeredIds = answer.multipleChoiceAnswers ?: emptyList()
                val correctOptions = options?.filter { it.correct }?.map { it.id } ?: emptyList()

                // Correct if all correct options are selected and no extra options are selected
                answeredIds.toSet() == correctOptions.toSet()
            }
        }

        return AnswerResponse(
            isCorrect,
            feedback,
            options?.filter { it.correct }?.map { it.text },
            range?.min,
            range?.max
        )
    }
}
