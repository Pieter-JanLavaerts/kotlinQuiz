package com.example.quiz.data.mappers

import com.example.quiz.data.*
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

fun QuestionCreateDTO.toEntity(): QuestionEntity = QuestionEntity(
    null,
    title,
    description,
    feedback,
    Question.Type.valueOf(type.toString()),
    options?.map {
        QuestionOptionEntity(
            null,
            it,
            correctAnswers?.contains(it) == true
        )
    } ?: emptyList(),
    range?.min,
    range?.max,
)

fun QuestionUpdateDTO.toEntity(id: UUID): QuestionEntity = QuestionEntity(
    id,
    title,
    description,
    feedback,
    Question.Type.valueOf(type.toString()),
    options?.map {
        QuestionOptionEntity(
            null,
            it,
            correctAnswers?.contains(it) == true
        )
    } ?: emptyList(),
    range?.min,
    range?.max,
)

