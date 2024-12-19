package su.pank.solver.domain

import kotlinx.serialization.Serializable
import su.pank.solver.data.model.ProbabilityFileCalculation
import su.pank.solver.data.model.Symbol

/**
 * Кодировка по Хаффману
 */
class HuffmanCodingUseCase {

    operator fun invoke(fileCalculation: ProbabilityFileCalculation): HuffmanResult {
        val result = huffmanTree(fileCalculation.symbols)
        return HuffmanResult(fileCalculation.hash, result.first, result.first.toSymbolEncodeList(), result.second)
    }

    // Главная функция постройки дереве
    fun huffmanTree(symbols: List<Symbol>): Pair<HuffmanSymbolEncode, List<HuffmanStep>> {
        // Преобразование в символы Хаффмана и сортируем
        var symbols = symbols.map {
            HuffmanSymbolEncode(it.char, it.probability)
        }.sortedByDescending {
            it.probability
        }
        val steps = mutableListOf(HuffmanStep(symbols))
        while (symbols.size != 1) {
            // Берём два последних символа складываем и указываем как дочерние

            val lastTwoSymbols = symbols.takeLast(2)

            symbols = (symbols.dropLast(2) + HuffmanSymbolEncode(
                char = lastTwoSymbols[1].char,
                probability = lastTwoSymbols[0].probability + lastTwoSymbols[1].probability,
                childrenPair = Pair(lastTwoSymbols[1], lastTwoSymbols[0])
            )).sortedByDescending {
                it.probability
            }
            steps.add(
                HuffmanStep(symbols)
            )
        }
        return symbols[0] to steps
    }
}

// Получение таблицы символов
fun HuffmanSymbolEncode.toSymbolEncodeList(code: String = ""): List<SymbolEncoded> {
    return this.childrenPair?.let {
        it.first.toSymbolEncodeList(code + "1") + it.second.toSymbolEncodeList(code + "0")
    } ?: listOf(SymbolEncoded(this.char, code, probability))
}


// Шаг Хаффмана
@Serializable
data class HuffmanStep(val symbols: List<HuffmanSymbolEncode>)


// Общий результат по файлу
@Serializable
data class HuffmanResult(
    val hash: String,
    val graphData: HuffmanSymbolEncode,
    val tableData: List<SymbolEncoded>,
    val steps: List<HuffmanStep>
){
    val averageNumberOfBinaryDigits
        get() = tableData.sumOf { it.code.length * it.probability.toDouble() }
}

// Зашифровоанный символ по Хаффману
@Serializable
data class HuffmanSymbolEncode(
    val char: Char,
    val probability: Float,
    val childrenPair: Pair<HuffmanSymbolEncode, HuffmanSymbolEncode>? = null
)