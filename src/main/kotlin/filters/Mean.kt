package org.example.filters

class Mean: Scheme(
    factor = 1.0 / 9.0,
    bias = 0.0,
    matrix = arrayOf(
        doubleArrayOf(1.0, 1.0, 1.0),
        doubleArrayOf(1.0, 1.0, 1.0),
        doubleArrayOf(1.0, 1.0, 1.0),
    ),
)
