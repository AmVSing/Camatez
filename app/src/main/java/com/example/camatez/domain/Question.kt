package com.example.camatez.domain

data class Question(
    val lhs: Int,
    val rhs: Int,
    val op: Operation,
    val ans: Int,
) {
    override fun toString(): String = "$lhs $op $rhs"
}
