package su.pank.solver.data.calculation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import su.pank.solver.data.model.ProbabilityFileCalculation

class FakeProbabilityFileCalculationRepository: ProbabilityFileCalculationRepository {
    override val probabilityFileCalculations: Flow<List<ProbabilityFileCalculation>> = flow {
        emit(emptyList())
    }
    override val currentCalculation: Flow<ProbabilityFileCalculation?> = flow {
        emit(null)
    }

    override suspend fun setCurrentCalculation(calculation: ProbabilityFileCalculation) {
        TODO("Not yet implemented")
    }

    override suspend fun addFileCalculation(probabilityFileCalculation: ProbabilityFileCalculation) {
        TODO("Not yet implemented")
    }
}