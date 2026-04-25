package org.example.filters

class HorizontalEdges: Scheme (
    factor = 1.0,
    bias = 0.0,
    matrix = arrayOf(
        arrayOf(0, 0, -1, 0, 0),
        arrayOf(0, 0, -1, 0, 0),
        arrayOf(0, 0, 2, 0, 0),
        arrayOf(0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0),
    )
)