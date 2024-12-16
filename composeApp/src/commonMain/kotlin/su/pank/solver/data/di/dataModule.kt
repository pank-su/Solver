package su.pank.solver.data.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable
import org.koin.dsl.module
import su.pank.solver.data.calculation.DefaultFileCalculationRepository
import su.pank.solver.data.calculation.FileCalculationRepository

@OptIn(ExperimentalSettingsApi::class)
val dataModule = module {
    single<FileCalculationRepository> {
        DefaultFileCalculationRepository(get())
    }
    single<ObservableSettings> { Settings().makeObservable() // I FUCK ALL WORLD
     }
}