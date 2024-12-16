package su.pank.solver.domain.di

import org.koin.dsl.module
import su.pank.solver.domain.ProbabilityTableUseCase

val domainModule = module{
    single{
        ProbabilityTableUseCase(get())
    }
}