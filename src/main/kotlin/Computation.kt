package org.example

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.example.filters.Scheme
import kotlin.math.min

object Computation {
    var dispatcher: CoroutineDispatcher = Dispatchers.Default

    fun sequential(
        name: String,
        filter: Scheme,
    ) {
        val image = IOManager.loadRgbImage(name)

        val output = ByteArray(image.width * image.height * image.channels)

        Convolution.applyRange(
            0 until image.width * image.height,
            image.width,
            image.height,
            image.input,
            output,
            filter,
        )

        IOManager.saveRgbImage(name + "_" + filter.name, image.width, image.height, output)
    }

    suspend fun withCoroutinesSegments(
        name: String,
        filter: Scheme,
        numJobs: Int,
    ) {
        val image = IOManager.loadRgbImage(name)

        val output = ByteArray(image.width * image.height * image.channels)
        val total = image.width * image.height
        val batchSize = (total + numJobs - 1) / numJobs
        coroutineScope {
            val jobs =
                (0 until numJobs).map { i ->
                    launch(dispatcher) {
                        val start = i * batchSize
                        val end = min(start + batchSize, total)
                        Convolution.applyRange(
                            start until end,
                            image.width,
                            image.height,
                            image.input,
                            output,
                            filter,
                        )
                    }
                }

            jobs.joinAll()
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.width, image.height, output)
    }

    suspend fun withCoroutinesColumn(
        name: String,
        filter: Scheme,
        numJobs: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)

        val output = ByteArray(image.width * image.height * image.channels)

        val jobs = numJobs ?: image.width
        val tasks = (image.width + jobs - 1) / jobs

        coroutineScope {
            val jobsList =
                (0 until jobs).map { i ->
                    launch(dispatcher) {
                        val start = i * tasks
                        val end = min(start + tasks, image.width)
                        Convolution.applyChunk(
                            start until end,
                            0 until image.height,
                            image.width,
                            image.height,
                            image.input,
                            output,
                            filter,
                        )
                    }
                }

            jobsList.joinAll()
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.width, image.height, output)
    }

    suspend fun withCoroutinesRows(
        name: String,
        filter: Scheme,
        numJobs: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)

        val output = ByteArray(image.width * image.height * image.channels)

        val jobs = numJobs ?: image.height
        val tasks = (image.height + jobs - 1) / jobs

        coroutineScope {
            val jobsList =
                (0 until jobs).map { i ->
                    launch(dispatcher) {
                        val start = i * tasks
                        val end = min(start + tasks, image.height)
                        Convolution.applyChunk(
                            0 until image.width,
                            start until end,
                            image.width,
                            image.height,
                            image.input,
                            output,
                            filter,
                        )
                    }
                }

            jobsList.joinAll()
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.width, image.height, output)
    }

    suspend fun withCoroutinesChunk(
        name: String,
        filter: Scheme,
        numJobsX: Int,
        numJobsY: Int,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = ByteArray(image.width * image.height * image.channels)

        val batchSizeX = (image.width + numJobsX - 1) / numJobsX
        val batchSizeY = (image.height + numJobsY - 1) / numJobsY

        coroutineScope {
            val jobs =
                buildList {
                    for (i in 0 until numJobsX) {
                        for (j in 0 until numJobsY) {
                            add(
                                launch(dispatcher) {
                                    val startX = i * batchSizeX
                                    val endX = min((i + 1) * batchSizeX, image.width)
                                    val startY = j * batchSizeY
                                    val endY = min((j + 1) * batchSizeY, image.height)
                                    Convolution.applyChunk(
                                        startX until endX,
                                        startY until endY,
                                        image.width,
                                        image.height,
                                        image.input,
                                        output,
                                        filter,
                                    )
                                },
                            )
                        }
                    }
                }

            jobs.joinAll()
        }

        IOManager.saveRgbImage(name + "_" + filter.name, image.width, image.height, output)
    }
}
