package su.pank.solver.domain

import kotlinx.serialization.Serializable
import su.pank.solver.data.model.ProbabilityFileCalculation
import su.pank.solver.data.model.Symbol
import kotlin.math.abs

/**
 * Реализация алгоритма Шеннона-Фано с сохранением промежуточных шагов.
 */
class ShanonFanoCodingUseCase {

    operator fun invoke(probabilityFileCalculation: ProbabilityFileCalculation): ShanonFanoResult {
        return shanonFano(probabilityFileCalculation.symbols)
    }

    /**
     * Деление списка символов на две части по сумме вероятностей.
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

    /**
     * Основной метод для кодирования Шеннона-Фано.
     */
    private fun shanonFano(symbols: List<Symbol>): ShanonFanoResult {
        val sortedSymbols = symbols.sortedByDescending { it.probability }
        val (part1, part2) = splitList(sortedSymbols)
        return encodeShanonFano(part1, part2).sorted()
    }

    /**
     * Рекурсивный метод для кодирования.
     */
    private fun encodeShanonFano(
        part1: List<Symbol>,
        part2: List<Symbol>,
        code: String = "",
        steps: Set<ShanonStep> = emptySet()
    ): ShanonFanoResult {
        val steps = steps + ShanonStep(part1, part2)


        val part1Encoded = if (part1.size != 1) {
            val (split1, split2) = splitList(part1)
            encodeShanonFano(split1, split2, code + "1", steps)
        } else {
            ShanonFanoResult(
                listOf(SymbolEncoded(part1.first().char, code + "1", part1.first().probability)),
                steps.toList()
            )
        }

        val part2Encoded = if (part2.size != 1) {
            val (split1, split2) = splitList(part2)
            encodeShanonFano(split1, split2, code + "0", steps)
        } else {
            ShanonFanoResult(
                listOf(SymbolEncoded(part2.first().char, code + "0", part2.first().probability)),
                steps.toList()
            )
        }


        return (part1Encoded + part2Encoded)
    }
}

/**
 * Результат кодирования Шеннона-Фано.
 *
 * @property encodedSymbols Список закодированных символов с их кодами и вероятностями.
 * @property steps Список шагов деления списка символов на части.
 */
@Serializable
data class ShanonFanoResult(
    val encodedSymbols: List<SymbolEncoded>,
    val steps: List<ShanonStep>
) {

    val averageNumberOfBinaryDigits
        get() = encodedSymbols.sumOf { it.code.length * it.probability.toDouble() }

    /**
     * Объединяет текущий результат с другим результатом кодирования.
     *
     * @param other Другой результат кодирования.
     * @return Новый результат, содержащий объединенные символы и шаги.
     */
    operator fun plus(other: ShanonFanoResult): ShanonFanoResult {
        return ShanonFanoResult(
            encodedSymbols + other.encodedSymbols,
            steps + other.steps
        )
    }



    /**
     * Сортирует закодированные символы и шаги.
     *
     * @return Отсортированный результат кодирования.
     */
    fun sorted(): ShanonFanoResult {
        return copy(
            encodedSymbols = encodedSymbols,
            steps = steps.distinct()
        )
    }
}

/**
 * Шаг кодирования Шеннона-Фано.
 *
 * @property part1 Первая часть списка символов после деления.
 * @property part2 Вторая часть списка символов после деления.
 */
@Serializable
data class ShanonStep(val part1: List<Symbol>, val part2: List<Symbol>)

/**
 * Закодированный символ.
 *
 * @property char Символ.
 * @property code Код символа.
 * @property probability Вероятность символа.
 */
@Serializable
data class SymbolEncoded(val char: Char, val code: String, val probability: Float)
