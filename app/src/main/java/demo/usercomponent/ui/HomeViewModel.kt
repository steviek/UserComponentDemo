package demo.usercomponent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.usercomponent.user.UserProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userProfileRepository: UserProfileRepository,
    greetingProvider: GreetingProvider,
) : ViewModel() {

    val state =
        userProfileRepository
            .flowDetails()
            .map { greetingProvider.getGreeting() + " from " + it.name }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
