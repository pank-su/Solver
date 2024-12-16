package su.pank.solver.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel


@Serializable
object Main

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()

    val state by viewModel.state.collectAsState()


    when (state) {
        MainState.FileNotPicked -> {
            FilePicker(onFilePicked = {
                viewModel.onFilePicked(it)
            })
        }

        MainState.Loading -> {
        }

        is MainState.PickedFile -> {
            MainNavigation((state as MainState.PickedFile).fileName, (state as MainState.PickedFile).fileText)
        }
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

