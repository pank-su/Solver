package su.pank.solver.ui.main.another_table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository

class AnotherTableViewModel(private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository) : ViewModel() {
    val data = probabilityFileCalculationRepository.currentCalculation.shareIn(
        viewModelScope,
        SharingStarted.Lazily, 1
    )
}