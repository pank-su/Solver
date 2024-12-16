package su.pank.solver.data.di

import org.koin.dsl.module
import su.pank.solver.data.calculation.FileCalculationRepository
import su.pank.solver.data.calculation.DefaultFileCalculationRepository

val dataModule = module{
    single<FileCalculationRepository>{
        DefaultFileCalculationRepository()
    }
}