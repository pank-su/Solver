package su.pank.solver.domain

import co.touchlab.kermit.Logger
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.first
import org.kotlincrypto.macs.hmac.sha2.HmacSHA256
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository
import su.pank.solver.data.model.ProbabilityFileCalculation
import su.pank.solver.data.model.Symbol
import kotlin.math.log2


class ProbabilityTableUseCase(private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository) {
    // Символы по которым рассчитываем вероятность
    val symbols = "0123456789абвгдеёжзийклмнопрстуфхцчшщъыьэюя .,:;-("

    @OptIn(ExperimentalStdlibApi::class)
    suspend operator fun invoke(file: PlatformFile): ProbabilityFileCalculation {
        // Хэшируем по SHA256
        val bytesArray = file.readBytes()
        val hash = HmacSHA256(bytesArray).doFinal().toHexString()

        Logger.d(
            "hash: $hash"
        )

        // Если файл с таким хэшем уже существует то возвращаем значение
        probabilityFileCalculationRepository.probabilityFileCalculations.first()
            .firstOrNull { calculation -> calculation.hash == hash }
            ?.let { calculation ->
                probabilityFileCalculationRepository.setCurrentCalculation(calculation)
                return calculation
            }


        // получаем текст файл
        val text = bytesArray.decodeToString().lowercase()

        // считаем все символы
        var symbols = buildList {
            symbols.forEach { ch ->
                add(Symbol(ch, text.count { textCh -> textCh == ch }, 0f, 0f))
            }
        }

        // сколько всего символов
        val occurrencesSum = symbols.sumOf { symbol -> symbol.occurrences }

        // вероятность и информация по каждому символу
        symbols = symbols.map { symbol ->
            val probality = symbol.occurrences / occurrencesSum.toFloat()
            symbol.copy(probability = probality, information = if (probality != 0f) -log2(probality) else 0f)
        }

        val calculation = ProbabilityFileCalculation(
            hash,
            symbols
        )

        // сохраняем и выбираем
        probabilityFileCalculationRepository.addFileCalculation(calculation)
        probabilityFileCalculationRepository.setCurrentCalculation(calculation)
        return calculation
    }
}