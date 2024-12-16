package su.pank.solver.ui.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import su.pank.solver.ui.main.MainViewModel
import su.pank.solver.ui.main.entropy_table.EntropyTableViewModel

val uiModule = module {
    viewModel {
        MainViewModel(get())
    }
    viewModel{
        EntropyTableViewModel(get())
    }
}