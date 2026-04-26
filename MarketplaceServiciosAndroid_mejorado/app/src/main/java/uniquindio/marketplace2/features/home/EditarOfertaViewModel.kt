package uniquindio.marketplace2.features.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uniquindio.marketplace2.data.repositorios.RepositorioOfertas
import javax.inject.Inject

@HiltViewModel
class EditarOfertaViewModel @Inject constructor(
    val repositorioOfertas: RepositorioOfertas
) : ViewModel()
