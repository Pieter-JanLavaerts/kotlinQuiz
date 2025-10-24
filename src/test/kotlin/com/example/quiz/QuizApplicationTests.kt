package com.example.quiz

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [TestConfig::class])
class QuizApplicationTests {

	@Test
	fun contextLoads() {
	}

}
