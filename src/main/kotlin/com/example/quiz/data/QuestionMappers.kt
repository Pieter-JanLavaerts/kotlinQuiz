package com.example.quiz.data

import com.example.quiz.model.*
import java.util.UUID

fun QuestionEntity.toDomain(): Question = when (type) {
    Question.Type.multiple_choice, Question.Type.single_choice ->
        Question.create(
            id!!,
            title,
            description,
            feedback,
            type,
            options.map { it.toDomain() },
            null
        )
    Question.Type.range ->
        Question.create(
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

fun QuestionCreateDTO.toDomain(): Question = Question.create(
    null,
    title,
    description,
    feedback,
    Question.Type.valueOf(type.toString()),
    options?.map { text ->
        QuestionOption(UUID.randomUUID(), text, correctAnswers?.contains(text) == true)
    } ?: emptyList(),
    range?.let { QuestionRange(it.min!!, it.max!!) }
)

fun QuestionUpdateDTO.toDomain(id: UUID): Question = Question.create(
        id,
        title,
        description,
        feedback,
        Question.Type.valueOf(type.toString()),
        options?.map { text ->
            QuestionOption(UUID.randomUUID(), text, correctAnswers?.contains(text) == true)
        } ?: emptyList(),
        range?.let { QuestionRange(it.min!!, it.max!!) }
    )

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

fun Question.toDTO(): QuestionDTO = QuestionDTO(
    title,
    description,
    feedback,
    QuestionDTO.Type.valueOf(type.toString()),
    id,
    options?.map { it.toDTO() },
    options?.filter { it.correct }?.map { it.text },
    range?.let { QuestionAllOfRangeDTO(it.min, it.max) }
)

fun QuestionOption.toDTO(): QuestionOptionDTO =
    QuestionOptionDTO(id, text)

fun QuestionSummary.toDTO(): QuestionSummaryDTO =
    QuestionSummaryDTO(id, title, QuestionSummaryDTO.Type.valueOf(type.toString()))
