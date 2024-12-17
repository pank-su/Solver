package su.pank.solver.ui.main.huffman

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import su.pank.solver.ui.main.ScreenOfMain
import su.pank.solver.ui.main.shanon_fano.CodeTable

@Serializable
object HuffmanCoding : ScreenOfMain {
    override val icon: ImageVector = Icons.Default.QrCode

    override val label: String
        get() = "Хаффман"
}


@Composable
fun HuffmanCodingScreen() {
    val vm = koinViewModel<HuffmanCodingViewModel>()
    val scrollState = rememberScrollState()
    val result by vm.result.collectAsState(null)

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Row {
            OutlinedTextField(vm.message, {
                vm.message = it
            }, placeholder = { Text("Введите сообщение") })
            IconButton(onClick = { vm.encodeMessage() }) {
                Icon(Icons.Default.Done, null)
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Text(vm.encodedMessage)
        Spacer(modifier = Modifier.height(50.dp))

        if (result == null) {
            CircularProgressIndicator()
        } else {
            CodeTable(result!!.tableData)
        }



    }
}