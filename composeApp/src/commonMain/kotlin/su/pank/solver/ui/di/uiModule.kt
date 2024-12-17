package su.pank.solver.ui.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import su.pank.solver.ui.main.MainViewModel
import su.pank.solver.ui.main.another_table.AnotherTableViewModel
import su.pank.solver.ui.main.entropy_table.EntropyTableViewModel
import su.pank.solver.ui.main.huffman.HuffmanCodingViewModel
import su.pank.solver.ui.main.shanon_fano.ShanonFanoCodingScreen
import su.pank.solver.ui.main.shanon_fano.ShanonFanoCodingViewModel

val uiModule = module {
    viewModel {
        MainViewModel(get())
    }
    viewModel{
        EntropyTableViewModel(get())
    }
    viewModel {
        AnotherTableViewModel(get())
    }
    viewModel {
        ShanonFanoCodingViewModel(get())
    }
    viewModel{
        HuffmanCodingViewModel(get())
    }
}