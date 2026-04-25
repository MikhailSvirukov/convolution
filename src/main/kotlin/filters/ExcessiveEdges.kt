package org.example.filters

class ExcessiveEdges: Scheme(
    factor = 1.0,
    bias = 0.0,
    matrix = arrayOf(arrayOf(1, 1, 1), arrayOf(1, -7, 1), arrayOf(1, 1, 1)),
)