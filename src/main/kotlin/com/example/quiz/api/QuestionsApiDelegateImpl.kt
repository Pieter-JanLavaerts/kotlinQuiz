package com.example.quiz.api

import com.example.quiz.data.QuestionService
import com.example.quiz.data.toDTO
import com.example.quiz.data.toDomain
import com.example.quiz.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.util.*


@Component
class QuestionsApiDelegateImpl(
    private val questionService: QuestionService
) : QuestionsApiDelegate {

    override fun questionsGet(): ResponseEntity<List<QuestionSummaryDTO>> = ResponseEntity(
        questionService.getQuestionSummaries().map { it.toDTO() }, HttpStatus.OK
    )

    override fun questionsIdGet(id: UUID): ResponseEntity<QuestionDTO> =
        questionService.getQuestion(id)
            ?.let { ResponseEntity.ok(it.toDTO()) }
            ?: ResponseEntity.notFound().build()


    override fun questionsPost(questionCreateDTO: QuestionCreateDTO): ResponseEntity<QuestionDTO> {
        try {
            val question = questionCreateDTO.toDomain()
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionService.createQuestion(question)?.toDTO())
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.message, e
            )
        }
    }

    override fun questionsIdPut(id: UUID, questionUpdateDTO: QuestionUpdateDTO): ResponseEntity<QuestionDTO> {
        try {
            val question = questionUpdateDTO.toDomain(id)
            return ResponseEntity.status(HttpStatus.OK)
                .body(questionService.updateQuestion(id, question)?.toDTO())
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.message, e
            )
        }
    }

    override fun questionsIdDelete(id: UUID): ResponseEntity<Unit> {
        questionService.deleteQuestion(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    override fun questionsIdAnswerPost(
        id: UUID,
        answerRequestDTO: AnswerRequestDTO
    ): ResponseEntity<AnswerResponseDTO> {
        val response = questionService.answerQuestion(id, AnswerRequest(
            answerRequestDTO.multipleChoiceAnswer,
            answerRequestDTO.rangeAnswer))
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        return ResponseEntity.status(HttpStatus.OK).body(
            AnswerResponseDTO(
                response.correct,
                response.feedback,
                response.correctAnswersMultipleChoice,
                if (response.correctAnswersRangeMin != null) AnswerResponseRangeAnswerDTO(response.correctAnswersRangeMin, response.correctAnswersRangeMax) else null
            )
        )
    }
}