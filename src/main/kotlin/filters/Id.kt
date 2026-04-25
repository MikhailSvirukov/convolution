package org.example.filters

class Id :
    Scheme(
        name = "id",
        factor = 1.0,
        bias = 0.0,
        matrix =
            arrayOf(
                doubleArrayOf(1.0, 1.0, 1.0),
                doubleArrayOf(1.0, 1.0, 1.0),
                doubleArrayOf(1.0, 1.0, 1.0),
            ),
    )
