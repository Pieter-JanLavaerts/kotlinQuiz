package com.example.quiz.data.mappers

import com.example.quiz.data.QuestionEntity
import com.example.quiz.data.QuestionOptionEntity
import com.example.quiz.model.*
import java.util.UUID

fun QuestionEntity.toDomain(): Question = when (type) {
    Question.Type.multiple_choice, Question.Type.single_choice ->
        Question(
            id!!,
            title,
            description,
            feedback,
            type,
            options.map { it.toDomain() },
            null
        )
    Question.Type.range ->
        Question(
            id!!,
            title,
            description,
            feedback,
            type,
            null,
            QuestionRange(rangeMin!!, rangeMax!!)
        )
}

fun QuestionOptionEntity.toDomain(): QuestionOption =
    QuestionOption(id!!, text, correct)

fun QuestionCreateDTO.toDomain(): Question = when (type) {
    QuestionCreateDTO.Type.multiple_choice,
    QuestionCreateDTO.Type.single_choice -> Question(
        UUID.randomUUID(), // temporary, not persisted yet
        title,
        description,
        feedback,
        Question.Type.valueOf(type.toString()),
        options?.map { text ->
            QuestionOption(UUID.randomUUID(), text, correctAnswers?.contains(text) == true)
        } ?: emptyList(),
        null
    )

    QuestionCreateDTO.Type.range -> Question(
        UUID.randomUUID(),
        title,
        description,
        feedback,
        Question.Type.range,
        null,
        range?.let { QuestionRange(it.min!!, it.max!!) }
    )
}

fun QuestionUpdateDTO.toDomain(id: UUID): Question = when (type) {
    QuestionUpdateDTO.Type.multiple_choice,
    QuestionUpdateDTO.Type.single_choice -> Question(
        id,
        title,
        description,
        feedback,
        Question.Type.valueOf(type.toString()),
        options?.map { text ->
            QuestionOption(UUID.randomUUID(), text, correctAnswers?.contains(text) == true)
        } ?: emptyList(),
        null
    )

    QuestionUpdateDTO.Type.range -> Question(
        id,
        title,
        description,
        feedback,
        Question.Type.range,
        null,
        range?.let { QuestionRange(it.min!!, it.max!!) }
    )
}

fun Question.toEntity(): QuestionEntity = when (type) {
    Question.Type.multiple_choice, Question.Type.single_choice ->
        QuestionEntity(
            id,
            title,
            description,
            feedback,
            type,
            options?.map {
                QuestionOptionEntity(null, it.text, it.correct)
            } ?: emptyList(),
            null,
            null
        )

    Question.Type.range ->
        QuestionEntity(
            id,
            title,
            description,
            feedback,
            type,
            emptyList(),
            range?.min,
            range?.max
        )
}
