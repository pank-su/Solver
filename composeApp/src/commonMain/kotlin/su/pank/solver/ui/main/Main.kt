package su.pank.solver.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel


@Serializable
object Main

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()


    when (state) {
        MainState.FileNotPicked -> {
            FilePicker(onFilePicked = viewModel::onFilePicked)
        }

        MainState.Loading -> {

        }

        is MainState.PickedFile -> {

            MainNavigation((state as MainState.PickedFile).fileName, (state as MainState.PickedFile).fileText)
        }
    }


    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(Modifier.height(16.dp))

    }

}

@Composable
fun FilePicker(onFilePicked: (PlatformFile) -> Unit) {
    val launcher = rememberFilePickerLauncher(
        mode = PickerMode.Single,
        type = PickerType.File(extensions = listOf("txt"))
    ) { file ->
        if (file != null) {
            onFilePicked(file)
        }
    }

    Box(Modifier.fillMaxSize()) {

        Button(onClick = { launcher.launch() }, modifier = Modifier.align(Alignment.Center)) {
            Text("Выберите файл")
            Icon(Icons.Default.FileUpload, null)
        }
    }


}

@Composable
fun TableEntropy(symbols: String) {

}

