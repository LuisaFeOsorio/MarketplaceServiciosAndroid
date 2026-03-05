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
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id =R.mipmap.logo),
            contentDescription = "ServiMarket"
        )

        Text(text = "MARKETPLACE")

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
                onClick = onNavigateToRegister
            ) {
                Text(text = "Crear una cuenta")
            }
        }
    }
}