package su.pank.solver.data

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings

object DefaultSettings {
    @OptIn(ExperimentalSettingsApi::class)
    val settings = Settings() as ObservableSettings

}