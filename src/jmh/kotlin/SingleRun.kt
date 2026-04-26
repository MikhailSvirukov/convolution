package org.example.benchmark

import Executor
import LoadedImage
import kotlinx.coroutines.runBlocking
import org.example.filters.Scheme
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.util.concurrent.TimeUnit

const val JOBS = 8

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@Fork(1)
open class SingleRun {
    val executor = Executor()
//"excessive_edges", "sharpen1", "emboss",
    @Param("img/bench/bird.png")
    lateinit var img: String
    @Param("blur")
    private lateinit var schemeName: String

    lateinit var scheme: Scheme
    lateinit var loadedImage: LoadedImage

    @Setup(Level.Trial)
    fun setup() {
        scheme = Scheme.fromKey(schemeName)
        loadedImage = IOManager.loadRgbImage(img)
    }

    @Benchmark
    fun sequential() {
        Computation.sequential(loadedImage, scheme)
    }

    @Benchmark
    fun coroutinesRows() = runBlocking {
        Computation.withCoroutinesRows(loadedImage, scheme, null)
    }

    @Benchmark
    fun coroutinesColumns() = runBlocking {
        Computation.withCoroutinesColumn(loadedImage, scheme, null)
    }
    @Benchmark
    fun coroutinesSegment() = runBlocking {
        Computation.withCoroutinesSegments(loadedImage, scheme, JOBS)
    }
    @Benchmark
    fun coroutinesChunks() = runBlocking {
        Computation.withCoroutinesChunk(loadedImage, scheme, JOBS, JOBS)
    }
}