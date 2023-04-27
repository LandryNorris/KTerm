import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.BufferedReader

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    val state = rememberShellState("(landry) ~ ")
    MaterialTheme(typography = Typography(defaultFontFamily = FontFamily.Monospace)) {
        ShellEditor(state, darkThemeShellColors) {
            val command = state.currentCommand

            println("Command is $command")
            state.submitCommand(CommandState(
                buildAnnotatedString { append(state.ps1) },
                command, ""))

            val processBuilder = ProcessBuilder("/bin/sh", "-c", command)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = BufferedReader(process.inputReader())

            reader.forEachLine {
                println(it)
                state.appendLastCommandText(it)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
