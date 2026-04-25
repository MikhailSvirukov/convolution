package org.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.filters.Scheme
import kotlin.math.min

object Computation {
    val scope = CoroutineScope(Dispatchers.Default)

    fun sequential(
        name: String,
        filter: Scheme,
    ) {
        val image = IOManager.loadRgbImage(name)

        val output = ByteArray(image.width * image.height * image.channels)

        Convolution.applyRange(
            0..image.width * image.height,
            image.width,
            image.height,
            image.input,
            output,
            filter,
        )

        IOManager.saveRgbImage(name + "_" + filter.name, image.height, image.height, output)
    }

    fun withCoroutinesSegments(
        name: String,
        filter: Scheme,
        numJobs: Int,
    ) {
        val image = IOManager.loadRgbImage(name)

        val output = ByteArray(image.width * image.height * image.channels)
        val total = image.width * image.height
        val batchSize = total / numJobs

        for (i in 0 until numJobs) {
            scope.launch {
                Convolution.applyRange(
                    i * batchSize..min((i + 1) * batchSize, total),
                    image.width,
                    image.height,
                    image.input,
                    output,
                    filter,
                )
            }
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.height, image.height, output)
    }

    fun withCoroutinesColumn(
        name: String,
        filter: Scheme,
        numJobs: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = ByteArray(image.width * image.height * image.channels)

        val tasks = image.width / (numJobs ?: 1)

        for (i in 0 until (numJobs ?: image.width)) {
            scope.launch {
                Convolution.applyChunk(
                    0 until image.width,
                    i * tasks until min((i + 1) * tasks, image.height),
                    image.width,
                    image.height,
                    image.input,
                    output,
                    filter,
                )
            }
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.height, image.height, output)
    }

    fun withCoroutinesRows(
        name: String,
        filter: Scheme,
        numJobs: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = ByteArray(image.width * image.height * image.channels)

        val tasks = image.height / (numJobs ?: 1)

        for (i in 0 until (numJobs ?: image.height)) {
            scope.launch {
                Convolution.applyChunk(
                    i * tasks until min((i + 1) * tasks, image.width),
                    0 until image.height,
                    image.width,
                    image.height,
                    image.input,
                    output,
                    filter,
                )
            }
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.height, image.height, output)
    }

    fun withCoroutinesChunk(
        name: String,
        filter: Scheme,
        numJobsX: Int,
        numJobsY: Int,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = ByteArray(image.width * image.height * image.channels)

        val batchSizeX = image.width / numJobsX
        val batchSizeY = image.height / numJobsX

        for (i in 0 until numJobsX) {
            for (j in 0 until numJobsY) {
                scope.launch {
                    Convolution.applyChunk(
                        i * batchSizeX until min((i + 1) * batchSizeX, image.width),
                        j * batchSizeY until min((j + 1) * batchSizeY, image.height),
                        image.width,
                        image.height,
                        image.input,
                        output,
                        filter,
                    )
                }
            }
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.height, image.height, output)
    }
}
