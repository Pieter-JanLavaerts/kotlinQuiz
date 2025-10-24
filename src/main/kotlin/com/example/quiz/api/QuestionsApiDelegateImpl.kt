package com.example.quiz.api

import com.example.quiz.data.QuestionService
import com.example.quiz.model.AnswerRequest
import com.example.quiz.model.AnswerRequestDTO
import com.example.quiz.model.AnswerResponseDTO
import com.example.quiz.model.AnswerResponseRangeAnswerDTO
import com.example.quiz.model.QuestionAllOfRangeDTO
import com.example.quiz.model.QuestionCreateDTO
import com.example.quiz.model.QuestionDTO
import com.example.quiz.model.QuestionOptionDTO
import com.example.quiz.model.QuestionSummaryDTO
import com.example.quiz.model.QuestionUpdateDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class QuestionsApiDelegateImpl : QuestionsApiDelegate {

    @Autowired
    lateinit var questionService: QuestionService

    override fun questionsGet(): ResponseEntity<List<QuestionSummaryDTO>> = ResponseEntity(
        questionService.getQuestionSummaries().map {
            QuestionSummaryDTO(
                it.id,
                it.title,
                QuestionSummaryDTO.Type.valueOf(it.type.toString())
            )
        }, HttpStatus.OK
    )

    override fun questionsIdGet(id: UUID): ResponseEntity<QuestionDTO> {
        val question = questionService.getQuestion(id)
            ?: return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()

        val questionDTO = QuestionDTO(
            question.title,
            question.description,
            question.feedback,
            QuestionDTO.Type.valueOf(question.type.toString()),
            question.id,
            question.options?.map { QuestionOptionDTO(it.id, it.text)},
            question.options?.filter { it.correct }?.map { it.text },
            QuestionAllOfRangeDTO(question.range?.min, question.range?.max)
        )

        return ResponseEntity.status(HttpStatus.OK).body(questionDTO)
    }

    override fun questionsPost(questionCreateDTO: QuestionCreateDTO): ResponseEntity<QuestionDTO> {
        val question = questionService.createQuestion(questionCreateDTO)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        val questionDTO = QuestionDTO(
            question.title,
            question.description,
            question.feedback,
            QuestionDTO.Type.valueOf(question.type.toString()),
            question.id,
            question.options?.map { QuestionOptionDTO(it.id, it.text) },
            question.options?.filter { it.correct }?.map { it.text },
            QuestionAllOfRangeDTO(question.range?.min, question.range?.max)
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(questionDTO)
    }

    override fun questionsIdPut(id: UUID, questionUpdateDTO: QuestionUpdateDTO): ResponseEntity<QuestionDTO> {
        val question = questionService.updateQuestion(id, questionUpdateDTO)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        val questionDTO = QuestionDTO(
            question.title,
            question.description,
            question.feedback,
            QuestionDTO.Type.valueOf(question.type.toString()),
            question.id,
            question.options?.map { QuestionOptionDTO(it.id, it.text) },
            question.options?.filter { it.correct }?.map { it.text }
        )

        return ResponseEntity.status(HttpStatus.OK).body(questionDTO)
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
                AnswerResponseRangeAnswerDTO(response.correctAnswersRangeMin, response.correctAnswersRangeMax)
            )
        )
    }
}