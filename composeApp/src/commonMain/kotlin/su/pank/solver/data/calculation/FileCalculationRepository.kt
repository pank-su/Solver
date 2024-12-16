package su.pank.solver.data.calculation

import kotlinx.coroutines.flow.Flow
import su.pank.solver.data.model.FileCalculation

interface FileCalculationRepository {
    val fileCalculations: Flow<List<FileCalculation>>

    val currentCalculation: Flow<FileCalculation?>


    suspend fun setCurrentCalculation(calculation: FileCalculation)

    suspend fun addFileCalculation(fileCalculation: FileCalculation)
}