package org.example.filters

class AllEdges: Scheme(
    factor = 1.0,
    bias = 0.0,
    matrix = arrayOf(
        arrayOf(-1, -1, -1),
        arrayOf(-1, 8, -1),
        arrayOf(-1, -1, -1),
    )
)