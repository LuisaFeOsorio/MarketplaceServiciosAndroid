package uniquindio.marketplace2.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.core.datastore.SessionManager
import uniquindio.marketplace2.core.datastore.UserSession
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _sesion = MutableStateFlow<UserSession?>(null)
    val sesion: StateFlow<UserSession?> = _sesion.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.getUserInfo().collect { info ->
                _sesion.value = info
            }
        }
    }
}
