package com.example.quiz.data

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface QuestionRepository : CrudRepository<QuestionEntity, UUID>