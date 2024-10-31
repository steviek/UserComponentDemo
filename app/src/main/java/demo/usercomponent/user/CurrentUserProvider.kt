package demo.usercomponent.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CurrentUserProvider @Inject constructor() {
    private val activeUser = 1

    fun getCurrentUsers(): List<UserId> = listOf(activeUser)

    fun flowActiveUser(): Flow<UserId> = flowOf(activeUser)
}