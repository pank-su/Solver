package su.pank.solver.data.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable
import org.koin.dsl.module
import su.pank.solver.data.calculation.DefaultProbabilityFileCalculationRepository
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository
import su.pank.solver.data.fano.ShanonFanoCodingRepository
import su.pank.solver.data.huffman.HuffmanCodingRepository

@OptIn(ExperimentalSettingsApi::class)
val dataModule = module {
    single<ProbabilityFileCalculationRepository> {
        DefaultProbabilityFileCalculationRepository(get())
    }
    single<ObservableSettings> { Settings().makeObservable() // I FUCK ALL WORLD
     }

    single {
        ShanonFanoCodingRepository(get(), get(), get())
    }
    single {
        HuffmanCodingRepository(get(), get(), get())
    }
}