package com.example.camatez.domain

import kotlin.random.Random

class QuestionGenerator(
    private val rng: Random = Random.Default,
) {
    fun generate(settings: Settings): Question {
        val operation =
            settings.enabledOperations
                .filter { operation -> canGenerate(settings, operation) }
                .randomOrNull(rng)
                ?: throw IllegalArgumentException("No valid questions can be generated for the current settings.")

        return when (operation) {
            Operation.ADD -> generateAdd(settings)
            Operation.SUBTRACT -> generateSubtract(settings)
            Operation.MULTIPLY -> generateMultiply(settings)
            Operation.DIVIDE -> generateDivide(settings)
        }
    }

    private fun canGenerate(
        settings: Settings,
        operation: Operation,
    ): Boolean =
        when (operation) {
            Operation.ADD -> settings.addLhsRange.nonZeroValues().isNotEmpty() && settings.addRhsRange.nonZeroValues().isNotEmpty()
            Operation.SUBTRACT -> validSubtractRhs(settings).isNotEmpty()
            Operation.MULTIPLY -> settings.mulLhsRange.nonZeroValues().isNotEmpty() && settings.mulRhsRange.nonZeroValues().isNotEmpty()
            Operation.DIVIDE -> validDivideRhs(settings).isNotEmpty()
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
        val lhsCandidates = settings.addLhsRange.nonZeroValues()
        val rhsCandidates = validSubtractRhs(settings)
        require(rhsCandidates.isNotEmpty()) { "No valid subtraction questions can be generated." }
        val rhs = rhsCandidates.random(rng)
        val validLhs = lhsCandidates.filter { it >= rhs }
        require(validLhs.isNotEmpty()) { "No valid subtraction lhs values for rhs=$rhs in ${settings.addLhsRange}" }
        val lhs = validLhs.random(rng)

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
        val rhsCandidates = validDivideRhs(settings)
        require(rhsCandidates.isNotEmpty()) { "No valid division questions can be generated." }
        val rhs = rhsCandidates.random(rng)
        val lhsCandidates = settings.mulLhsRange.nonZeroValues().filter { it % rhs == 0 }
        val lhs = lhsCandidates.random(rng)

        return Question(
            lhs = lhs,
            rhs = rhs,
            op = Operation.DIVIDE,
            ans = lhs / rhs,
        )
    }

    private fun IntRange.randint(): Int {
        val candidates = nonZeroValues()
        require(candidates.isNotEmpty()) { "Invalid range" }
        return candidates.random(rng)
    }

    private fun IntRange.nonZeroValues(): List<Int> = this.filter { it != 0 }

    private fun validSubtractRhs(settings: Settings): List<Int> {
        val lhsCandidates = settings.addLhsRange.nonZeroValues()
        return settings.addRhsRange.nonZeroValues().filter { rhs ->
            lhsCandidates.any { lhs -> lhs >= rhs }
        }
    }

    private fun validDivideRhs(settings: Settings): List<Int> {
        val lhsCandidates = settings.mulLhsRange.nonZeroValues()
        return settings.mulRhsRange.nonZeroValues().filter { rhs ->
            lhsCandidates.any { lhs -> lhs % rhs == 0 }
        }
    }
}
