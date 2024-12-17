import su.pank.solver.domain.HuffmanCodingUseCase
import su.pank.solver.domain.toSymbolEncodeList
import kotlin.test.Test

class HuffmanCodingTest {

    val huffmanCodingUseCase = HuffmanCodingUseCase()

    @Test
    fun huffmanCodingIsValid(){
        val testData = generateTestSymbols(0.22f, 0.2f, 0.16f, 0.16f, 0.1f, 0.1f, 0.04f, 0.02f)

        huffmanCodingUseCase.huffmanTree(testData).toSymbolEncodeList().sortedByDescending { it.probability }.forEach {
            println("${it.code} ${it.char + 1}")
        }

    }
}