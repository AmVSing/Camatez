package com.example.camatez.domain

import kotlin.random.Random

class QuestionGenerator(
    private val rng: Random = Random.Default,
) {
    fun generate(settings: Settings): Question {
        val operation = settings.enabledOperations.random(rng)

        return when (operation) {
            Operation.ADD -> generateAdd(settings)
            Operation.SUBTRACT -> generateSubtract(settings)
            Operation.MULTIPLY -> generateMultiply(settings)
            Operation.DIVIDE -> generateDivide(settings)
        }
    }

    private fun generateAdd(settings: Settings): Question {
        val lhs = settings.addLhsRange.randint()
        val rhs = settings.addRhsRange.randint()

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.ADD,
            ans = lhs + rhs,
        )
    }

    private fun generateSubtract(settings: Settings): Question {
        val rhs = settings.addRhsRange.randint()
        val lhsCandidates = settings.addLhsRange.filter { it != 0 && it >= rhs }
        require(lhsCandidates.isNotEmpty()) {
            "No valid subtraction lhs values for rhs=$rhs in ${settings.addLhsRange}"
        }
        val lhs = lhsCandidates.random(rng)

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.SUBTRACT,
            ans = lhs - rhs,
        )
    }

    private fun generateMultiply(settings: Settings): Question {
        val lhs = settings.mulLhsRange.randint()
        val rhs = settings.mulRhsRange.randint()

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.MULTIPLY,
            ans = lhs * rhs,
        )
    }

    private fun generateDivide(settings: Settings): Question {
        val rhs = settings.mulRhsRange.randint()
        val lhsCandidates = settings.mulLhsRange.filter { it != 0 && it % rhs == 0 }
        require(lhsCandidates.isNotEmpty()) {
            "No valid division lhs values for rhs=$rhs in ${settings.mulLhsRange}"
        }
        val lhs = lhsCandidates.random(rng)

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.DIVIDE,
            ans = lhs / rhs,
        )
    }

    private fun IntRange.randint(): Int {
        val candidates = this.filter { it != 0 }
        require(candidates.isNotEmpty()) { "Invalid range" }
        return candidates.random(rng)
    }
}
