package su.pank.solver.domain

import co.touchlab.kermit.Logger
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.first
import org.kotlincrypto.macs.hmac.sha2.HmacSHA256
import su.pank.solver.data.calculation.FileCalculationRepository
import su.pank.solver.data.model.FileCalculation
import su.pank.solver.data.model.Symbol
import kotlin.math.log2

class ProbabilityTableUseCase(private val fileCalculationRepository: FileCalculationRepository) {
    val symbols = "0123456789абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,:;-()"

    @OptIn(ExperimentalStdlibApi::class)
    suspend operator fun invoke(file: PlatformFile): FileCalculation {
        val bytesArray = file.readBytes()
        val hash = HmacSHA256(bytesArray).doFinal().toHexString()

        Logger.d(
            "hash: $hash"
        )

        fileCalculationRepository.fileCalculations.first().firstOrNull { calculation -> calculation.hash == hash }
            ?.let { calculation ->
                fileCalculationRepository.setCurrentCalculation(calculation)
                return calculation
            }

        val text = bytesArray.decodeToString().lowercase()

        var symbols = buildList {
            symbols.forEach { ch ->
                add(Symbol(ch, text.count { textCh -> textCh == ch }, 0f, 0f))
            }
        }

        val occurrencesSum = symbols.sumOf { symbol -> symbol.occurrences }

        symbols = symbols.map { symbol ->
            val probality = symbol.occurrences / occurrencesSum.toFloat()
            symbol.copy(probability = probality, information = -log2(probality)) }

        val calculation = FileCalculation(
            hash,
            symbols
        )

        fileCalculationRepository.addFileCalculation(calculation)
        fileCalculationRepository.setCurrentCalculation(calculation)
        return calculation
    }
}