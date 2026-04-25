import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.filters.Scheme
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Files
import java.util.Comparator
import java.util.stream.Stream
import kotlin.test.assertContentEquals

class ComputationTest {
    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesSegments matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        Computation.dispatcher = Dispatchers.Unconfined

        val segments = Computation.withCoroutinesSegments(image, filter, 8)

        assertContentEquals(reference, segments)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesColumn matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        Computation.dispatcher = Dispatchers.Unconfined

        val columns = Computation.withCoroutinesColumn(image, filter, null)

        assertContentEquals(reference, columns)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesRows matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)
        Computation.dispatcher = Dispatchers.Unconfined

        val rows = Computation.withCoroutinesRows(image, filter, null)

        assertContentEquals(reference, rows)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesChunk matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        Computation.dispatcher = Dispatchers.Unconfined

        val chunks = Computation.withCoroutinesChunk(image, filter, 4, 4)

        assertContentEquals(reference, chunks)
    }

    companion object {
        @JvmStatic
        fun imageFilterCases(): Stream<Arguments> {
            val images =
                Files.list(File("img/test").toPath()).use { paths ->
                    paths
                        .filter { Files.isRegularFile(it) }
                        .filter { it.fileName.toString().endsWith(".jpg") }
                        .sorted(Comparator.comparing { it.fileName.toString() })
                        .map { it.toString() }
                        .toList()
                }

            return images
                .stream()
                .flatMap { imagePath ->
                    Scheme.all.stream().map { filter ->
                        Arguments.of(imagePath, filter)
                    }
                }
        }
    }
}
