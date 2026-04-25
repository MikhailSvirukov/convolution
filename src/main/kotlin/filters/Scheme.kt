package org.example.filters

abstract class Scheme(
    open val factor: Double,
    open val bias: Double,
    open val matrix: Array<Array<Number>>
)
