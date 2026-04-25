package org.example.filters

abstract class Scheme(
    val name: String,
    val factor: Double,
    val bias: Double,
    val matrix: Array<DoubleArray>,
)
