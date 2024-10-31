package demo.usercomponent.user

import demo.usercomponent.di.user.User
import demo.usercomponent.di.user.UserScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@UserScoped
class UserProfileRepository @Inject constructor(
    userId: UserId,
    @User private val defaultScope: CoroutineScope,
) {
    private val detailsFlow = MutableStateFlow(UserProfile(userId, "Arthur Dent"))
    private val mutex = Mutex()

    fun setName(name: String) {
        defaultScope.launch {
            mutex.withLock {
                detailsFlow.value = detailsFlow.value.copy(name = name)
            }
        }
    }

    fun flowDetails(): Flow<UserProfile> {
        return detailsFlow.asStateFlow()
    }
}

data class UserProfile(val userId: UserId, val name: String)