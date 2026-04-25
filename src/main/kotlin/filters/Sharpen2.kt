package org.example.filters

class Sharpen2 : Scheme(
    factor = 1.0 / 8.0,
    bias = 0.0,
    matrix = arrayOf(
        arrayOf(-1, -1, -1, -1, -1),
        arrayOf(-1, 2, 2, 2, -1),
        arrayOf(-1, 2, 8, 2, -1),
        arrayOf(-1, 2, 2, 2, -1),
        arrayOf(-1, -1, -1, -1, -1),
    )
)