package su.pank.solver.data.model

import androidx.annotation.FloatRange
import kotlinx.serialization.Serializable
import kotlin.math.ceil
import kotlin.math.log2

@Serializable
data class ProbabilityFileCalculation(val hash: String, val symbols: List<Symbol>) {
    val symbolsCount: Int
        get() = symbols.sumOf { symbol -> symbol.occurrences }

    val probabilitySum: Float
        get() = symbols.sumOf { symbol -> symbol.probability.toDouble() }.toFloat()


    val entropy: Float
        get() = symbols.sumOf { symbol -> symbol.probability.toDouble() * symbol.information }.toFloat()

    // Разраядность
    val capacity: Int
        get() = ceil(entropyMax).toInt()

    val entropyMax: Float
        get() = log2(symbols.size.toFloat())

    val absoluteRedundancy: Float
        get() = entropyMax - entropy

    val relativeRedundancy: Float
        get() = (absoluteRedundancy) / entropyMax

}

@Serializable
data class Symbol(
    val char: Char,
    val occurrences: Int,
    @FloatRange(from = 0.0, to = 1.0) val probability: Float,
    val information: Float
)

