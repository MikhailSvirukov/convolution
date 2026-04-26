import kotlinx.coroutines.test.runTest
import org.example.filters.Id
import org.example.filters.Scheme
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Files
import java.util.stream.Stream
import kotlin.test.assertContentEquals

class ComputationTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("imageFilterCases")
    fun `id filter keeps source image unchanged`(imagePath: String) =
        runTest {
            val image = IOManager.loadRgbImage(imagePath)
            val output = Computation.sequential(image, Id())
            val outputRow = Computation.withCoroutinesRows(image, Id(), null)
            val outputCol = Computation.withCoroutinesColumn(image, Id(), null)
            val outputSeg = Computation.withCoroutinesSegments(image, Id(), NUMBJOBS)
            val outputChunk =
                Computation.withCoroutinesChunk(
                    image,
                    Id(),
                    NUMBJOBS,
                    NUMBJOBS,
                )
            assertContentEquals(image.input, output, "image: $imagePath sequentially")
            assertContentEquals(image.input, outputRow, "image: $imagePath by rows")
            assertContentEquals(image.input, outputCol, "image: $imagePath by columns")
            assertContentEquals(image.input, outputSeg, "image: $imagePath by segments")
            assertContentEquals(image.input, outputChunk, "image: $imagePath by chunks")
        }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesSegments matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runTest {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val segments = Computation.withCoroutinesSegments(image, filter, NUMBJOBS)

        assertContentEquals(reference, segments)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesColumn matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runTest {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val columns = Computation.withCoroutinesColumn(image, filter, null)

        assertContentEquals(reference, columns)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesRows matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runTest {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val rows = Computation.withCoroutinesRows(image, filter, null)

        assertContentEquals(reference, rows)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesChunk matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runTest {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val chunks = Computation.withCoroutinesChunk(image, filter, NUMBJOBS, NUMBJOBS)

        assertContentEquals(reference, chunks)
    }

    companion object {
        private const val NUMBJOBS = 8

        fun imagePaths(): List<String> =
            Files.list(File("img/test").toPath()).use { paths ->
                paths
                    .map { it.toString() }
                    .toList()
            }

        @JvmStatic
        fun imageFilterCases(): Stream<Arguments> =
            imagePaths()
                .stream()
                .flatMap { imagePath ->
                    Scheme.all.stream().map { filter ->
                        Arguments.of(imagePath, filter)
                    }
                }
    }
}
