package demo.usercomponent.di.user

import dagger.BindsInstance
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent
import demo.usercomponent.user.UserId
import javax.inject.Scope

@Scope
annotation class UserScoped

@UserScoped
@DefineComponent(parent = SingletonComponent::class)
interface UserComponent

@DefineComponent.Builder
interface UserComponentBuilder {
    fun userId(@BindsInstance userId: UserId): UserComponentBuilder
    fun userLifecycle(@BindsInstance userLifecycle: UserLifecycle): UserComponentBuilder
    fun build(): UserComponent
}
