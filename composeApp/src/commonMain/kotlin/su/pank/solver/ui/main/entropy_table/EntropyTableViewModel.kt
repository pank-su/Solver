package su.pank.solver.ui.main.entropy_table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository


class EntropyTableViewModel(private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository) :
    ViewModel() {
    val data = probabilityFileCalculationRepository.currentCalculation.shareIn(
        viewModelScope,
        SharingStarted.Lazily, 1
    )


    suspend fun generateCSV(): String {
            val data = data.filterNotNull().first()
            return buildString {
                appendLine("№,Символ,Код символа,Число вхождения в текст,Вероятность,I")
                data.symbols.forEachIndexed { index, symbol ->
                    appendLine("${index + 1},\"${symbol.char}\",${symbol.char.code},${symbol.occurrences},${symbol.probability},${symbol.information}")
                }
                appendLine(",,Всего символов в тексте:,${data.symbols.size},,")
                appendLine(",,,Полная вероятность:,${data.probabilitySum},")
                appendLine(",,,,Энтропия источника:,${data.entropy}")
            }
    }

}