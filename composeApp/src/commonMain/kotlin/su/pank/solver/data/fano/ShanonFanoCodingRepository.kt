package su.pank.solver.data.fano

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository
import su.pank.solver.domain.ShanonFanoCodingUseCase
import su.pank.solver.domain.ShanonFanoResult

/**
 * Хранение вычислений по Фано
 */
class ShanonFanoCodingRepository(
    private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository,
    private val settings: ObservableSettings,
    private val shanonFanoCodingUseCase: ShanonFanoCodingUseCase // FIXME: Не очень хорошо использовать use-case в репозитории
) {
    private val key = "fano"

    val current = probabilityFileCalculationRepository.currentCalculation.filterNotNull().map { calculation ->
        Json.decodeFromString<Map<String, ShanonFanoResult>>(settings.getString(key, "{}"))[
            calculation.hash
        ]?.let {
            return@map it
        }
        val res = shanonFanoCodingUseCase(calculation)
        add(calculation.hash, res)
        return@map res
    }

    suspend fun add(hash: String, shanonFanoResult: ShanonFanoResult) {
        settings[key] = Json.encodeToString(
            Json.decodeFromString<Map<String, ShanonFanoResult>>(
                settings.getString(
                    key,
                    "{}"
                )
            )
        )
    }
}

