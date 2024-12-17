import su.pank.solver.data.model.Symbol
import su.pank.solver.domain.ShanonFanoCodingUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ShanonFanoCodingTest {

    val shanonFanoCodingUseCase = ShanonFanoCodingUseCase()

    fun generateTestSymbols(vararg probabilities: Float) = buildList<Symbol> {
        probabilities.forEachIndexed { index, probability ->
            add(Symbol("$index"[0], 1, probability, 1f))
        }
    }

    fun List<Symbol>.toProbabilities() = this.map { it.probability }

    fun Pair<List<Symbol>, List<Symbol>>.toProbabilities() =
        Pair(this.first.toProbabilities(), this.second.toProbabilities())

    // Тестирование на корректное разделение массива
    @Test
    fun splitListByEqualProbabilityIsValid() {
        val testData = mapOf(
            generateTestSymbols(0.22f, 0.2f, 0.16f, 0.1f, 0.1f, 0.04f, 0.02f)
                    to Pair(
                listOf(0.22f, 0.2f),
                listOf(0.16f, 0.1f, 0.1f, 0.04f, 0.02f)
            ),
            generateTestSymbols(0.5f, 0.5f) to Pair(listOf(0.5f), listOf(0.5f)),
            generateTestSymbols(0.5f, 0.25f, 0.25f) to Pair(listOf(0.5f), listOf(0.25f, 0.25f)),
            generateTestSymbols(0.25f, 0.25f, 0.20f, 0.2f, 0.1f) to Pair(
                listOf(0.25f, 0.25f),
                listOf(0.20f, 0.2f, 0.1f)
            )

        )

        testData.forEach { data ->
            val result = shanonFanoCodingUseCase.splitList(data.key)
            assertEquals<Pair<List<Float>, List<Float>>>(data.value, result.toProbabilities())
        }


    }

    @Test
    fun shanonCodingTest() {
        val testData = generateTestSymbols(0.22f, 0.2f, 0.16f, 0.16f, 0.1f, 0.1f, 0.04f, 0.02f)



        shanonFanoCodingUseCase.shanonFano(testData).forEach {
            println(it.char + " " + it.code)
        }

    }
}