package com.example.lexpro_mobile


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexpro_mobile.apiRepository.ApiRepos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthEvents {
    data class Authorise(val login: String, val pass: String) : AuthEvents()

}

data class AuthState(
    val token: Int = 0,
    val jsID: String = "",
    val isLoading: Boolean = false,
    var login: String = "",
    val isAuthorised: Boolean = false
)

@HiltViewModel
class ViewModel @Inject constructor(private val repos: ApiRepos) : ViewModel() {
    var authState by mutableStateOf(AuthState())
        private set


    fun onEvent(event: AuthEvents) {
        var code = 0
        when (event) {
            is AuthEvents.Authorise -> {
                authState = authState.copy(isLoading = true)
                viewModelScope.launch {
                    val list = repos.postApi(event.login, event.pass)
                    when (list[0].toString()) {
                        "302" -> {
                            authState = authState.copy(isLoading = false, jsID = list[1].toString(), token = 302)
                        }
                        "200" -> {
                            authState = authState.copy(isLoading = false, jsID = "None", token = 200)
                        }
                        "400" -> {
                            authState = authState.copy(isLoading = false, jsID = "None", token = 400)
                        }
                    }
                }
            }

            else -> {}
        }
    }


    fun getStageList() {
        viewModelScope.launch(Dispatchers.IO) {
            repos.getStage()
        }
    }


}

