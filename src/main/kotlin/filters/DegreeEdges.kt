package org.example.filters

class DegreeEdges: Scheme(
    factor = 1.0,
    bias = 0.0,
    matrix = arrayOf(
        arrayOf(-1, 0, 0, 0, 0),
        arrayOf(0, -2, 0, 0, 0),
        arrayOf(0, 0, 6, 0, 0),
        arrayOf(0, 0, 0, -2, 0),
        arrayOf(0, 0, 0, 0, -1),
    )
)