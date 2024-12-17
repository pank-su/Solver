package su.pank.solver.data.fano

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository
import su.pank.solver.domain.ShanonFanoCodingUseCase
import su.pank.solver.domain.SymbolEncoded

class ShanonFanoCodingRepository(
    private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository,
    private val settings: ObservableSettings,
    private val shanonFanoCodingUseCase: ShanonFanoCodingUseCase // FIXME: Не очень хорошо использовать use-case в репозитории
) {
    val current = probabilityFileCalculationRepository.currentCalculation.filterNotNull().map { calculation ->
        Json.decodeFromString<List<ShanonFanoCoding>>(settings.getString("fano", "[]")).firstOrNull {
            it.hash == calculation.hash
        }?.let {
            return@map it
        }
        val encodedSymbols = shanonFanoCodingUseCase(calculation)
        val rez = ShanonFanoCoding(calculation.hash, encodedSymbols)
        add(rez)
        return@map rez
    }

    suspend fun add(shanonFanoCoding: ShanonFanoCoding) {
        settings["fano"] = Json.encodeToString(
            Json.decodeFromString<List<ShanonFanoCoding>>(
                settings.getString(
                    "fano",
                    "[]"
                )
            )
        )
    }
}

@Serializable
data class ShanonFanoCoding(val hash: String, val encodedSymbols: List<SymbolEncoded>)