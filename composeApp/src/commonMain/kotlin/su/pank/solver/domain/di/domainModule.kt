package su.pank.solver.domain.di

import org.koin.dsl.module
import su.pank.solver.domain.HuffmanCodingUseCase
import su.pank.solver.domain.ProbabilityTableUseCase
import su.pank.solver.domain.ShanonFanoCodingUseCase

val domainModule = module{
    single{
        ProbabilityTableUseCase(get())
    }
    single {
        ShanonFanoCodingUseCase()
    }
    single {
        HuffmanCodingUseCase()
    }
}