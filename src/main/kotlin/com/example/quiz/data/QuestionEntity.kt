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
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import java.math.BigDecimal
import java.util.UUID

@Entity
data class QuestionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    val title: String,
    val description: String,
    val feedback: String,
    val type: Type,
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "question_id")
    val options: List<QuestionOptionEntity>,
    val rangeMin: BigDecimal?,
    val rangeMax: BigDecimal?,
)
