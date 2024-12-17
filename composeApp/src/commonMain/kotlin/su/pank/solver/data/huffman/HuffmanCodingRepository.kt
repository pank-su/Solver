package su.pank.solver.data.huffman

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository
import su.pank.solver.domain.HuffmanCodingUseCase
import su.pank.solver.domain.HuffmanResult

class HuffmanCodingRepository(
    private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository,
    private val huffmanCodingUseCase: HuffmanCodingUseCase,
    private val settings: ObservableSettings
) {
    val current = probabilityFileCalculationRepository.currentCalculation.filterNotNull().map { calculation ->
        Json.decodeFromString<List<HuffmanResult>>(settings.getString("huffman", "[]")).firstOrNull {
            it.hash == calculation.hash
        }?.let {
            return@map it
        }
        val rez = huffmanCodingUseCase(calculation)

        add(rez)
        return@map rez
    }

    suspend fun add(shanonFanoCoding: HuffmanResult) {
        settings["huffman"] = Json.encodeToString(
            Json.decodeFromString<List<HuffmanResult>>(
                settings.getString(
                    "huffman",
                    "[]"
                )
            )
        )
    }
}