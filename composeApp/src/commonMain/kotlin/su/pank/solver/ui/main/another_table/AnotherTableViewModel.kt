package su.pank.solver.ui.main.another_table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import su.pank.solver.data.calculation.ProbabilityFileCalculationRepository

class AnotherTableViewModel(private val probabilityFileCalculationRepository: ProbabilityFileCalculationRepository) :
    ViewModel() {
    val data = probabilityFileCalculationRepository.currentCalculation.shareIn(
        viewModelScope,
        SharingStarted.Lazily, 1
    )

    suspend fun generateCSV(): String {
        val data = data.filterNotNull().first()

        val headers = ",Неопределённость,Разрядность кода,Абсолютная избыточность,Относительная избыточность"

        val rows = listOf(
            "При кодировании сообщения стандартной кодовой таблицей ASCII,8 бит,8 бит,${8 - data.entropy},${(8 - data.entropy) / 8f}",
            "\"При использовании равномерного кода, построенного на основе меры Хартли\",${data.entropyMax} бит,${data.capacity} бит,${data.absoluteRedundancy},${data.relativeRedundancy}"
        )

        return (listOf(headers) + rows).joinToString("\n")


    }
}