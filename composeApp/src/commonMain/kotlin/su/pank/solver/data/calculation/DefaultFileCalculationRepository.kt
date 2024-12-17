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
import su.pank.solver.data.model.FileCalculation

class DefaultFileCalculationRepository(private val settings: ObservableSettings) : FileCalculationRepository {
    @OptIn(ExperimentalSettingsApi::class)
    override val fileCalculations: Flow<List<FileCalculation>> =
        settings.getStringFlow("calculations", "[]").map { value ->
            Json.decodeFromString(value)
        }

    private val _currentCalculation = MutableStateFlow<FileCalculation?>(null)

    override val currentCalculation: Flow<FileCalculation?> = _currentCalculation


    override suspend fun setCurrentCalculation(calculation: FileCalculation) {
        _currentCalculation.value = calculation
    }

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun addFileCalculation(fileCalculation: FileCalculation) {
        settings["calculations"] = Json.encodeToString(
            Json.decodeFromString<List<FileCalculation>>(
                settings.getString(
                    "calculations",
                    "[]"
                )
            ) + fileCalculation
        )

    }

    init {
        settings.clear()
    }


}