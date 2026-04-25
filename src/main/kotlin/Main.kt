@file:OptIn(ExperimentalCli::class)

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.optional
import kotlinx.coroutines.runBlocking
import org.example.filters.Scheme
import java.io.File

val schemeArgType =
    ArgType.Choice(
        choices = Scheme.all,
        toVariant = { Scheme.fromKey(it) },
        variantToString = { it.name },
    )

val executor = Executor()

fun main(args: Array<String>) {
    class CoroutinesChunks : Subcommand("coroutines_chunks", "Run coroutine chunk computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasksX by argument(ArgType.Int, "number of tasks to split for rows")
        private val tasksY by argument(ArgType.Int, "number of tasks to split for columns")

        override fun execute() {
            require(File(filename).exists()) { "File does not exist: $filename" }
            runBlocking { executor.withCoroutinesChunk(filename, filter, tasksX, tasksY) }
        }
    }

    class Sequential : Subcommand("sequential", "Run sequential computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")

        override fun execute() {
            require(File(filename).exists()) { "File does not exist: $filename" }
            executor.sequential(filename, filter)
        }
    }

    class CoroutinesRows : Subcommand("coroutines_rows", "Run coroutine rows computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasks by argument(ArgType.Int, "number of tasks to split file").optional()

        override fun execute() {
            require(File(filename).exists()) { "File does not exist: $filename" }
            runBlocking { executor.withCoroutinesRows(filename, filter, tasks) }
        }
    }

    class CoroutinesColumns : Subcommand("coroutines_columns", "Run coroutine columns computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasks by argument(ArgType.Int, "number of tasks to split file").optional()

        override fun execute() {
            require(File(filename).exists()) { "File does not exist: $filename" }
            runBlocking { executor.withCoroutinesColumn(filename, filter, tasks) }
        }
    }

    class CoroutinesSegment : Subcommand("coroutines_segment", "Run coroutine segment computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasks by argument(ArgType.Int, "number of tasks to split file")

        override fun execute() {
            require(File(filename).exists()) { "File does not exist: $filename" }
            runBlocking { executor.withCoroutinesSegments(filename, filter, tasks) }
        }
    }

    val parser = ArgParser("convolution")

    parser.subcommands(
        Sequential(),
        CoroutinesRows(),
        CoroutinesColumns(),
        CoroutinesSegment(),
        CoroutinesChunks(),
    )
    parser.parse(args)
}
