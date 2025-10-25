package com.example.quiz.data

import com.example.quiz.data.mappers.toDomain
import com.example.quiz.data.mappers.toEntity
import com.example.quiz.model.AnswerRequest
import com.example.quiz.model.AnswerResponse
import com.example.quiz.model.Question
import com.example.quiz.model.QuestionCreateDTO
import com.example.quiz.model.QuestionSummary
import com.example.quiz.model.QuestionUpdateDTO
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class QuestionService(private val db: QuestionRepository) {
    fun getQuestionSummaries(): List<QuestionSummary> = db.findAll().map {
        QuestionSummary(it.id!!, it.title, it.type)
    }

    fun getQuestion(id: UUID): Question? =
        db.findById(id).getOrNull()?.toDomain()

    fun createQuestion(createQuestion: QuestionCreateDTO): Question? =
        db.save(createQuestion.toEntity()).toDomain()

    fun updateQuestion(id: UUID, updateQuestion: QuestionUpdateDTO): Question? =
        db.save(updateQuestion.toEntity(id)).toDomain()

    fun deleteQuestion(id: UUID) = db.deleteById(id)

    fun answerQuestion(id: UUID, answer: AnswerRequest): AnswerResponse? =
        getQuestion(id)?.let { question ->
            val correct = when (question.type) {
                Question.Type.range ->
                    answer.rangeAnswer?.let { it in question.range!!.min..question.range.max } ?: false
                else -> {
                    val answeredCorrectly = answer.multipleChoiceAnswers?.all { ansId ->
                        question.options?.find { it.id == ansId }?.correct == true
                    } ?: false
                    answeredCorrectly &&
                            question.options?.filter { it.correct }?.size == answer.multipleChoiceAnswers?.size
                }
            }

            AnswerResponse(
                correct,
                question.feedback,
                question.options?.filter { it.correct }?.map { it.text },
                question.range?.min,
                question.range?.max
            )
        }
}