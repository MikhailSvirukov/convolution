package org.example.filters

class Emboss : Scheme(
    factor = 1.0,
    bias = 128.0,
    matrix = arrayOf(
        arrayOf(-1, -1, 0),
        arrayOf(-1, 0, 1),
        arrayOf(0, 1, 1),
    )
)