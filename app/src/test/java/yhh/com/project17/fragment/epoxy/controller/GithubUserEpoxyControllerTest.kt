package yhh.com.project17.fragment.epoxy.controller

import android.os.Build
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import yhh.com.repository.entity.GithubUserEntity

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O], manifest = Config.NONE)
class GithubUserEpoxyControllerTest {

    private lateinit var controller: GithubUserEpoxyController

    @Before
    fun setUp() {
        controller = GithubUserEpoxyController()
    }

    @Test
    fun emptyResultAdapter_oneEmptyHint() {
        controller.setData(listOf())

        assertEquals(1, controller.adapter.itemCount)
    }

    @Test
    fun adapter_with10Data() {
        controller.setData(listOf(
            GithubUserEntity(userId = 1),
            GithubUserEntity(userId = 2),
            GithubUserEntity(userId = 3),
            GithubUserEntity(userId = 4),
            GithubUserEntity(userId = 5),
            GithubUserEntity(userId = 6),
            GithubUserEntity(userId = 7),
            GithubUserEntity(userId = 8),
            GithubUserEntity(userId = 9),
            GithubUserEntity(userId = 10))
        )

        assertEquals(10, controller.adapter.itemCount)
    }
}