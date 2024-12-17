package su.pank.solver.data.calculation

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import su.pank.solver.data.model.ProbabilityFileCalculation

class DefaultProbabilityFileCalculationRepository(private val settings: ObservableSettings) : ProbabilityFileCalculationRepository {

    val json = Json {
        allowSpecialFloatingPointValues = true
    }

    @OptIn(ExperimentalSettingsApi::class)
    override val probabilityFileCalculations: Flow<List<ProbabilityFileCalculation>> =
        settings.getStringFlow("calculations", "[]").map { value ->
            json.decodeFromString(value)
        }

    private val _currentCalculation = MutableStateFlow<ProbabilityFileCalculation?>(null)

    override val currentCalculation: Flow<ProbabilityFileCalculation?> = _currentCalculation


    override suspend fun setCurrentCalculation(calculation: ProbabilityFileCalculation) {
        _currentCalculation.value = calculation
    }

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun addFileCalculation(probabilityFileCalculation: ProbabilityFileCalculation) {
        settings["calculations"] = json.encodeToString(
            json.decodeFromString<List<ProbabilityFileCalculation>>(
                settings.getString(
                    "calculations",
                    "[]"
                )
            ) + probabilityFileCalculation
        )

    }

    init {
        //settings.clear()
    }


}