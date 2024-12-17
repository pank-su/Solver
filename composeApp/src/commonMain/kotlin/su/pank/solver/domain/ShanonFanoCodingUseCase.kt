package su.pank.solver.domain

import kotlinx.serialization.Serializable
import su.pank.solver.data.model.ProbabilityFileCalculation
import su.pank.solver.data.model.Symbol
import kotlin.math.abs

/**
 * Алгоритм Shanon'a Fano с сохранением расчётов
 */
class ShanonFanoCodingUseCase {
    operator fun invoke(probabilityFileCalculation: ProbabilityFileCalculation): List<SymbolEncoded> =
        shanonFano(
            probabilityFileCalculation.symbols
        )


    /**
     * Деление списка на две части по сумме вероятностей
     */
    internal fun splitList(symbols: List<Symbol>, accuracy: Float = 0.0001f): Pair<List<Symbol>, List<Symbol>> {

        var probabilitySum = symbols.sumOf { it.probability.toDouble() }.toFloat()
        var sum = 0f // сумма элементов с начала списка
        val part1 = mutableListOf<Symbol>()
        val part2 = mutableListOf<Symbol>()
        var index = 0

        do {
            val symbol = symbols[index++]
            probabilitySum -= symbol.probability
            sum += symbol.probability
            part1.add(symbol)

        } while (abs(sum - probabilitySum) > accuracy && sum < probabilitySum)
        if (sum - probabilitySum > accuracy && part1.size > 1){
            part1.removeLastOrNull()
            index--
        }
        part2.addAll(symbols.subList(index, symbols.size))


        return Pair(part1, part2)
    }

    // first-step
    fun shanonFano(symbols: List<Symbol>): List<SymbolEncoded> {
        val symbols = symbols.sortedByDescending { it.probability }
        val splitted = splitList(symbols)
        return (shanonFano(splitted.first, splitted.second)).sortedByDescending { it.probability }
    }

    fun shanonFano(part1: List<Symbol>, part2: List<Symbol>, code: String = ""): List<SymbolEncoded> {
        val part1Encoded: List<SymbolEncoded>
        val part2Encoded: List<SymbolEncoded>

        if (part1.size != 1) {
            val splittedPart1 = splitList(part1)
            part1Encoded = shanonFano(splittedPart1.first, splittedPart1.second, code + "1")
        } else {
            part1Encoded = listOf(SymbolEncoded(part1[0].char, code + "1", part1[0].probability))
        }

        if (part2.size != 1) {
            val splittedPart2 = splitList(part2)
            part2Encoded = shanonFano(splittedPart2.first, splittedPart2.second, code + "0")
        } else {
            part2Encoded = listOf(SymbolEncoded(part2[0].char, code + "0", part2[0].probability))
        }

        return part1Encoded + part2Encoded
    }


}

@Serializable
data class SymbolEncoded(val char: Char, val code: String, val probability: Float)