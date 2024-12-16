package su.pank.solver.data.calculation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import su.pank.solver.data.model.FileCalculation

class FakeFileCalculationRepository: FileCalculationRepository {
    override val fileCalculations: Flow<List<FileCalculation>> = flow {
        emit(emptyList())
    }
    override val currentCalculation: Flow<FileCalculation?> = flow {
        emit(null)
    }

    override suspend fun setCurrentCalculation(calculation: FileCalculation) {
        TODO("Not yet implemented")
    }

    override suspend fun addFileCalculation(fileCalculation: FileCalculation) {
        TODO("Not yet implemented")
    }
}