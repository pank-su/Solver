package su.pank.solver.domain

import androidx.compose.ui.util.fastDistinctBy
import kotlinx.serialization.Serializable
import su.pank.solver.data.model.ProbabilityFileCalculation
import su.pank.solver.data.model.Symbol

class HuffmanCodingUseCase {
    operator fun invoke(fileCalculation: ProbabilityFileCalculation) {

    }

    fun huffmanTree(symbols: List<Symbol>): HuffmanSymbolEncode {
        var symbols = symbols.map {
            HuffmanSymbolEncode(it.char, it.probability)
        }
        while (symbols.size != 1) {
            symbols = symbols.sortedByDescending {
                it.probability
            }
            val lastTwoSymbols = symbols.takeLast(2)

            symbols = symbols.dropLast(2) + HuffmanSymbolEncode(
                char = lastTwoSymbols[0].char,
                probability = lastTwoSymbols[0].probability + lastTwoSymbols[1].probability,
                childrenPair = Pair(lastTwoSymbols[0], lastTwoSymbols[1],)
            )
        }
        return symbols[0]
    }




}

// Получение таблицы символов
fun HuffmanSymbolEncode.toSymbolEncodeList(code: String = ""): List<SymbolEncoded> {

    return this.childrenPair?.let {
        it.first.toSymbolEncodeList(code + "1") + it.second.toSymbolEncodeList(code + "0")
    } ?: listOf(SymbolEncoded(this.char,code, probability))


}
@Serializable
data class HuffmanSymbolEncode(
    val char: Char,
    val probability: Float,
    val childrenPair: Pair<HuffmanSymbolEncode, HuffmanSymbolEncode>? = null
)