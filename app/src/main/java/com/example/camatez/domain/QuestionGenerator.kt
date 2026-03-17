package com.example.camatez.domain

import kotlin.random.Random

class QuestionGenerator(
    private val rng: Random = Random.Default,
) {
    fun generate(config: Config): Question {
        val operation = config.enabledOperations.random(rng)

        return when (operation) {
            Operation.ADD -> generateAdd(config)
            Operation.SUBTRACT -> generateSubtract(config)
            Operation.MULTIPLY -> generateMultiply(config)
            Operation.DIVIDE -> generateDivide(config)
        }
    }

    private fun generateAdd(config: Config): Question {
        val lhs = config.addLhsRange.randint()
        val rhs = config.addRhsRange.randint()

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.ADD,
            ans = lhs + rhs,
        )
    }

    private fun generateSubtract(config: Config): Question {
        val rhs = config.addRhsRange.randint()
        val lhsCandidates = config.addLhsRange.filter { it != 0 && it >= rhs }
        require(lhsCandidates.isNotEmpty()) {
            "No valid subtraction lhs values for rhs=$rhs in ${config.addLhsRange}"
        }
        val lhs = lhsCandidates.random(rng)

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.SUBTRACT,
            ans = lhs - rhs,
        )
    }

    private fun generateMultiply(config: Config): Question {
        val lhs = config.mulLhsRange.randint()
        val rhs = config.mulRhsRange.randint()

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.MULTIPLY,
            ans = lhs * rhs,
        )
    }

    private fun generateDivide(config: Config): Question {
        val rhs = config.mulRhsRange.randint()
        val lhsCandidates = config.mulLhsRange.filter { it != 0 && it % rhs == 0 }
        require(lhsCandidates.isNotEmpty()) {
            "No valid division lhs values for rhs=$rhs in ${config.mulLhsRange}"
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