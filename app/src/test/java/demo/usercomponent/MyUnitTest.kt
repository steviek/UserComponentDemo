package demo.usercomponent

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class MyUnitTest {

    val rule
        @Rule
        get() = HiltAndroidRule(this)

    private val repository by userComponent { getUserProfileRepository() }

    @Before
    fun setUp() {
        rule.inject()
    }

    @Test
    fun name_updates() {
        repository.setName("Ford Prefect")

        val details = runBlocking { repository.flowDetails().first() }
        assertEquals("Ford Prefect", details.name)
    }
}