package org.example.filters

sealed class Scheme(
    val name: String,
    val factor: Double,
    val bias: Double,
    val matrix: Array<DoubleArray>,
) {
    companion object {
        val all =
            listOf(
                Id(),
                Zero(),
                Mean(),
                Blur(),
                ExcessiveEdges(),
                HorizontalEdges(),
                VerticalEdges(),
                Emboss(),
                Sharpen1(),
                Sharpen2(),
                DegreeEdges(),
            )

        fun fromKey(key: String) = all.find { it.name == key } ?: error("Unknown scheme: $key")
    }
}
