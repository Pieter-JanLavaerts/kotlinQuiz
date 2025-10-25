package com.example.quiz.data

import com.example.quiz.model.*
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

    fun createQuestion(question: Question): Question? {
        val saved = db.save(question.toEntity())
        return saved.toDomain()
    }

    fun updateQuestion(id: UUID, question: Question): Question? {
        val saved = db.save(question.toEntity())
        return saved.toDomain()
    }

    fun deleteQuestion(id: UUID) = db.deleteById(id)

    fun answerQuestion(id: UUID, answer: AnswerRequest): AnswerResponse? =
        getQuestion(id)?.evaluateAnswer(answer)
}
