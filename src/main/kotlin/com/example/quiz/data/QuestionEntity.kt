package com.example.quiz.data

import com.example.quiz.model.Question
import com.example.quiz.model.Question.Type
import com.example.quiz.model.QuestionOption
import com.example.quiz.model.QuestionRange
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.math.BigDecimal
import java.util.UUID

@Entity
data class QuestionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID?,
    val title: String,
    val description: String,
    val feedback: String,
    val type: Type,
    @OneToMany(cascade = [CascadeType.ALL])
    val options: List<QuestionOptionEntity>,
    val rangeMin: BigDecimal?,
    val rangeMax: BigDecimal?,
) {
    fun toDomain(): Question? = id?.let { nonNullId ->
        when (type) {
            Type.multiple_choice, Type.single_choice -> {
                Question(
                    nonNullId,
                    title,
                    description,
                    feedback,
                    type,
                    options.map { QuestionOption(it.id!!, it.text, it.correct) },
                    null
                )
            }

            Type.range -> {
                Question(
                    nonNullId,
                    title,
                    description,
                    feedback,
                    type,
                    null,
                    QuestionRange(rangeMin!!, rangeMax!!)
                )
            }
        }
    }
}
