package su.pank.solver.data.model

import androidx.annotation.FloatRange
import kotlinx.serialization.Serializable

@Serializable
data class FileCalculation(val hash: String, val symbols: List<Symbol>) {
    val symbolsCount: Int
        get() = symbols.sumOf { symbol -> symbol.occurrences }

    val probabilitySum: Float
        get() = symbols.sumOf { symbol -> symbol.probability.toDouble() }.toFloat()


    val infoAvg: Float
        get() = symbols.sumOf { symbol -> symbol.probability.toDouble() * symbol.information }.toFloat()
}

@Serializable
data class Symbol(
    val char: Char,
    val occurrences: Int,
    @FloatRange(from = 0.0, to = 1.0) val probability: Float,
    val information: Float
)

