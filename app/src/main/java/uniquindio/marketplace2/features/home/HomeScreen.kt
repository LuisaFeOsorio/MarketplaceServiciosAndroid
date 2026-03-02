package uniquindio;
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uniquindio.marketplace2.R

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,   // Función para navegar al Login
    onNavigateToRegister: () -> Unit  // Función para navegar al Registro
) {
    // La columna principal ocupa todo el espacio y centra su contenido
    Column(
        modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio disponible [4]
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically), // Espaciado y centrado vertical [4]
        horizontalAlignment = Alignment.CenterHorizontally // Centrado horizontal [4]
    ) {
        // Carga la imagen desde res/drawable/welcome.png [6, 7]
        Image(
            painter = painterResource(id =R.mipmap.logo),
            contentDescription = "ServiMarket"
        )

        // Texto de bienvenida [8]
        Text(text = "Pantalla de bienvenida")

        // Fila horizontal para organizar los botones de acción [5, 9]
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally), // Espacio entre botones [4]
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onNavigateToLogin
            ) {
                Text(text = "Iniciar sesión")
            }

            Button(
                onClick = onNavigateToRegister // Lógica de navegación externa [5]
            ) {
                Text(text = "Crear una cuenta")
            }
        }
    }
}