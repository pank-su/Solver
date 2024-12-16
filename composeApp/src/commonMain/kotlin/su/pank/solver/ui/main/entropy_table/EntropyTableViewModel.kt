package su.pank.solver.ui.main.entropy_table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import su.pank.solver.data.calculation.FileCalculationRepository




class EntropyTableViewModel(private val fileCalculationRepository: FileCalculationRepository): ViewModel() {
    val data =  fileCalculationRepository.currentCalculation.shareIn(viewModelScope,
        SharingStarted.Lazily, 1)
}