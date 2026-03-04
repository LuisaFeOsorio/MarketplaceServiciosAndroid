package uniquindio.marketplace2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uniquindio.marketplace2.core.theme.Marketplace2Theme
import uniquindio.marketplace2.features.login.LoginScreen

class MainActivity : ComponentActivity() {
    /*
 * Esta función se llama automáticamente cuando la actividad es creada. Aquí es donde configuramos el contenido de la actividad.
 */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llamar al método onCreate de la superclase
        super.onCreate(savedInstanceState)
        // Habilitar el modo Edge-to-Edge para una mejor experiencia visual
        enableEdgeToEdge()
        // Configurar el contenido de la actividad utilizando Jetpack Compose
        setContent {
            // Aplicar el tema personalizado de la aplicación
            Marketplace2Theme() {
                // Se define la estructura principal de la interfaz de usuario
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llamar a la función Greeting para mostrar el mensaje de bienvenida
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Función composable que muestra un mensaje de saludo
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// Función de vista previa para el editor de Android Studio. Esta función no se ejecuta en tiempo de ejecución, solo sirve para previsualizar la UI.
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Marketplace2Theme {
        Greeting("Android")
    }
}