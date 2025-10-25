package com.example.quiz.data.mappers

import com.example.quiz.model.Question
import com.example.quiz.model.QuestionAllOfRangeDTO
import com.example.quiz.model.QuestionDTO
import com.example.quiz.model.QuestionOption
import com.example.quiz.model.QuestionOptionDTO
import com.example.quiz.model.QuestionSummary
import com.example.quiz.model.QuestionSummaryDTO

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