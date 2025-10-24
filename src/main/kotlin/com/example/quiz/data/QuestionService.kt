package com.example.quiz.data

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
    fun getQuestionSummaries(): List<QuestionSummary> = db.findAll().map({
        QuestionSummary(
            it.id!!,
            it.title,
            Question.Type.valueOf(it.type.toString())
        )
    })

    fun getQuestion(id: UUID): Question? {
        val question = db.findById(id).getOrNull()
        return question?.toDomain()
    }

    fun createQuestion(createQuestion: QuestionCreateDTO): Question? = db.save(
        QuestionEntity(
        null,
        createQuestion.title,
        createQuestion.description,
        createQuestion.feedback,
        Question.Type.valueOf(createQuestion.type.toString()),
        createQuestion.options?.map {
            QuestionOptionEntity(
                null,
                it,
                createQuestion.correctAnswers?.contains(it) == true
            )
        } ?: emptyList(),
        createQuestion.range?.min,
        createQuestion.range?.max,
    )).toDomain()

    fun updateQuestion(id: UUID, updateQuestion: QuestionUpdateDTO): Question? = db.save(
        QuestionEntity(
            id,
            updateQuestion.title,
            updateQuestion.description,
            updateQuestion.feedback,
            Question.Type.valueOf(updateQuestion.type.toString()),
            updateQuestion.options?.map {
                QuestionOptionEntity(
                    null,
                    it,
                    updateQuestion.correctAnswers?.contains(it) == true
                )
            } ?: emptyList(),
            updateQuestion.range?.min,
            updateQuestion.range?.max,
        )).toDomain()

    fun deleteQuestion(id: UUID) = db.deleteById(id)

    fun answerQuestion(id: UUID, answer: AnswerRequest): AnswerResponse? {
        val question = getQuestion(id)
        return question?.let { question ->
            var correct = true
            if (answer.multipleChoiceAnswers.isNullOrEmpty()) {
                correct = question.range?.min!! < answer.rangeAnswer && answer.rangeAnswer!! < question.range.max
            } else {
                answer.multipleChoiceAnswers?.forEach { option ->
                    //TODO: currently this only checks if all answers are correct but not if all correct answers are answered
                    if (question.options?.find { it.id == option }?.correct == false) {
                        correct = false
                    }
                }
            }
            return AnswerResponse(
                correct,
                question.feedback,
                question.options?.map { it.text },
                question.range?.min,
                question.range?.max
            )
        }
    }
}