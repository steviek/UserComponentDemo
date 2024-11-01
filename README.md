# User Component Demo

This is a demo project showing usage of a custom Hilt Component with a 'copy module' to propagate bindings to the Activity and ViewModel components. This project
can be checked out to Android Studio, and you should be able to run the app or the test, but I've also included some code snippets and links here for convenience.

Talk given at [Droidcon London 2024](https://london.droidcon.com/steven-kideckel/)

## Creating a custom component

See the useful guide on [Hilt's site](https://dagger.dev/hilt/custom-components). Within this codebase, the component definition is 
[here](https://github.com/steviek/UserComponentDemo/blob/main/app/src/main/java/demo/usercomponent/di/user/UserComponent.kt) and the
components are created and managed [here](https://github.com/steviek/UserComponentDemo/blob/main/app/src/main/java/demo/usercomponent/di/user/UserComponentManager.kt#L20)

```kotlin
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
```

## Providing Coroutines

It's generally useful to be able to have work be automatically cancelled when the user logs out. The coroutine scope is provided in 
[this](https://github.com/steviek/UserComponentDemo/blob/main/app/src/main/java/demo/usercomponent/di/user/UserCoroutineModule.kt) module

```kotlin
@InstallIn(UserComponent::class)
@Module
object UserCoroutineModule {
    @Provides
    @UserScoped
    @User
    fun provideUserDefaultScope(lifecycle: UserLifecycle): CoroutineScope {
        val context = Dispatchers.Default + SupervisorJob()
        lifecycle.addOnClearedListener { context.cancel() }
        return CoroutineScope(context)
    }
}

@Qualifier
annotation class User
```

## Deriving the user component from the Activity / View Model

Handle things separately in your landing / sign up pages. Everything else can assume that there is a signed in user, propagated through the intent / saved state handle
Module is [here](https://github.com/steviek/UserComponentDemo/blob/main/app/src/main/java/demo/usercomponent/di/activity/ActivityUserComponentModule.kt)

```kotlin
@InstallIn(ActivityComponent::class)
@Module
object ActivityUserComponentModule {
    @Provides
    fun provideUserComponent(
        userComponentManager: UserComponentManager,
        intent: Intent,
    ): UserComponent {
        val userId = getUserIdFromIntent(intent)
        return userComponentManager.getUserComponent(userId)
    }
}
```

## Copy module

[This](https://github.com/steviek/UserComponentDemo/blob/main/app/src/main/java/demo/usercomponent/di/user/UserComponentCopyModule.kt) is where we copy bindings only available in `UserComponent` to be available in the activity / view model components:

```kotlin
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
@Module
object CopyModule {
    @Provides
    @Reusable
    fun provideEntryPoint(component: UserComponent): CopyModuleEntryPoint {
        return getEntryPoint<CopyModuleEntryPoint>(component)
    }

    @Provides
    fun provideUserProfileRepository(entryPoint: CopyModuleEntryPoint): UserProfileRepository {
        return entryPoint.getUserProfileRepository()
    }
}

@InstallIn(UserComponent::class)
@EntryPoint
interface CopyModuleEntryPoint {
    fun getUserProfileRepository(): UserProfileRepository
}
```

## Test delegate

Handy helper for accessing classes requiring a signed in user from your singleton component tests. Definition is [here](https://github.com/steviek/UserComponentDemo/blob/main/app/src/test/java/demo/usercomponent/UserComponentDelegate.kt)

```kotlin
fun <T> userComponent(
    factory: CopyModuleEntryPoint.() -> T
): Lazy<T> = lazy {
    // Use a test rule to set up the user in your tests
    val testUserId = getTestUserId()
    // Make use of the current test Context
    val context = ApplicationProvider.getApplicationContext<Context>()
    // Get this from an entry point
    val userComponentManager = getUserComponentManager(context)
    val component = userComponentManager.getUserComponent(testUserId)
    factory(getEntryPoint<CopyModuleEntryPoint>(component))
}
```
