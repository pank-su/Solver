package su.pank.solver.data.model

import androidx.annotation.FloatRange
import kotlinx.serialization.Serializable
import kotlin.math.ceil
import kotlin.math.log2

@Serializable
data class ProbabilityFileCalculation(val hash: String, val symbols: List<Symbol>) {
    // Количество символов
    val symbolsCount: Int
        get() = symbols.sumOf { symbol -> symbol.occurrences }

    // Сумма вероятности
    val probabilitySum: Float
        get() = symbols.sumOf { symbol -> symbol.probability.toDouble() }.toFloat()

    // Энтропия
    val entropy: Float
        get() = symbols.sumOf { symbol -> symbol.probability.toDouble() * symbol.information }.toFloat()

    // Разрядность
    val capacity: Int
        get() = ceil(entropyMax).toInt()

    // Максимальная энтропия (равновероятные события)
    val entropyMax: Float
        get() = log2(symbols.size.toFloat())

    // Абсолютная
    val absoluteRedundancy: Float
        get() = entropyMax - entropy

    // Относительная
    val relativeRedundancy: Float
        get() = (absoluteRedundancy) / entropyMax

}

/**
 * Символ
 */
@Serializable
data class Symbol(
    val char: Char,
    val occurrences: Int,
    @FloatRange(from = 0.0, to = 1.0) val probability: Float,
    val information: Float
)

