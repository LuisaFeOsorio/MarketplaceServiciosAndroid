package uniquindio.marketplace2.features.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uniquindio.marketplace2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    onBackPressed: () -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    val usuario by viewModel.usuario.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val error by viewModel.error.collectAsState()

    val nombreEdit by viewModel.nombreEdit.collectAsState()
    val telefonoEdit by viewModel.telefonoEdit.collectAsState()
    val descripcionEdit by viewModel.descripcionEdit.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) stringResource(R.string.perfil_editar) else stringResource(R.string.perfil_titulo)) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isEditing) viewModel.cancelarEdicion()
                        else onBackPressed()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.volver))
                    }
                },
                actions = {
                    if (!isEditing && usuario != null) {
                        IconButton(onClick = viewModel::iniciarEdicion) {
                            Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.editar))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (usuario == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.perfil_error_cargar))
                }
            } else {
                val usuarioActual = usuario

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Foto de perfil
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        AsyncImage(
                            model = usuarioActual?.fotoPerfil ?: "",
                            error = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = stringResource(R.string.perfil_foto),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (usuarioActual?.rol == "trabajador") {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.perfil_calificacion,
                                    usuarioActual.calificacionPromedio,
                                    usuarioActual.serviciosCompletados),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            if (isEditing) {
                                OutlinedTextField(
                                    value = nombreEdit,
                                    onValueChange = viewModel::onNombreEditChange,
                                    label = { Text(stringResource(R.string.perfil_nombre)) },
                                    leadingIcon = { Icon(Icons.Default.Person, null) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            } else {
                                Text(stringResource(R.string.perfil_nombre), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(usuarioActual?.nombre ?: "", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(stringResource(R.string.perfil_email), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(usuarioActual?.email ?: "", fontSize = 14.sp)

                            Spacer(modifier = Modifier.height(12.dp))

                            if (isEditing) {
                                OutlinedTextField(
                                    value = telefonoEdit,
                                    onValueChange = viewModel::onTelefonoEditChange,
                                    label = { Text(stringResource(R.string.perfil_telefono)) },
                                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            } else {
                                Text(stringResource(R.string.perfil_telefono), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(usuarioActual?.telefono ?: stringResource(R.string.perfil_no_registrado), fontSize = 14.sp)
                            }

                            if (usuarioActual?.rol == "trabajador") {
                                Spacer(modifier = Modifier.height(12.dp))
                                if (isEditing) {
                                    OutlinedTextField(
                                        value = descripcionEdit,
                                        onValueChange = viewModel::onDescripcionEditChange,
                                        label = { Text(stringResource(R.string.perfil_descripcion)) },
                                        leadingIcon = { Icon(Icons.Default.Description, null) },
                                        modifier = Modifier.fillMaxWidth(),
                                        minLines = 3
                                    )
                                } else {
                                    Text(stringResource(R.string.perfil_descripcion), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(usuarioActual.descripcionProfesional ?: stringResource(R.string.perfil_sin_descripcion), fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (error != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = error ?: stringResource(R.string.error_general),
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (isEditing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = viewModel::cancelarEdicion,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.perfil_cancelar))
                            }
                            Button(
                                onClick = viewModel::guardarCambios,
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text(stringResource(R.string.perfil_guardar))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            viewModel.cerrarSesion()
                            onCerrarSesion()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.perfil_cerrar_sesion))
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}