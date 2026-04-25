package org.example.filters

class Zero: Scheme(
    factor = 1.0,
    bias = 0.0,
    matrix = arrayOf(arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0)),
)