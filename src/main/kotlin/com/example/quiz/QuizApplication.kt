package com.example.quiz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

//@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@SpringBootApplication
class QuizApplication

fun main(args: Array<String>) {
	runApplication<QuizApplication>(*args)
}
