package su.pank.solver.ui.main.entropy_table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository




class EntropyTableViewModel(private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository): ViewModel() {
    val data =  probabilityFileCalculationRepository.currentCalculation.shareIn(viewModelScope,
        SharingStarted.Lazily, 1)
}