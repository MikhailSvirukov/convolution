package org.example.filters

class ExcessiveEdges: Scheme(
    factor = 1.0,
    bias = 0.0,
    matrix = arrayOf(
        doubleArrayOf(1.0, 1.0, 1.0),
        doubleArrayOf(1.0, -7.0, 1.0),
        doubleArrayOf(1.0, 1.0, 1.0),
    ),
)
