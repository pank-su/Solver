package su.pank.solver.data.calculation

import kotlinx.coroutines.flow.Flow
import su.pank.solver.data.model.ProbabilityFileCalculation

interface ProbabilityFileCalculationRepository {
    val probabilityFileCalculations: Flow<List<ProbabilityFileCalculation>>

    val currentCalculation: Flow<ProbabilityFileCalculation?>


    suspend fun setCurrentCalculation(calculation: ProbabilityFileCalculation)

    suspend fun addFileCalculation(probabilityFileCalculation: ProbabilityFileCalculation)
}