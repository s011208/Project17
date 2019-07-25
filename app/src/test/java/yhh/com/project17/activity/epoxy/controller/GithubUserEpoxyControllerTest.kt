package yhh.com.project17.activity.epoxy.controller

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
    fun emptyAdapter() {
        controller.setData(false, false, listOf())

        assertEquals(0, controller.adapter.itemCount)
    }

    @Test
    fun emptyResultAdapter_oneEmptyHint() {
        controller.setData(true, false, listOf())

        assertEquals(1, controller.adapter.itemCount)
    }

    @Test
    fun adapter_with10Data() {
        controller.setData(true, false, listOf(
            GithubUserEntity(id = 1),
            GithubUserEntity(id = 2),
            GithubUserEntity(id = 3),
            GithubUserEntity(id = 4),
            GithubUserEntity(id = 5),
            GithubUserEntity(id = 6),
            GithubUserEntity(id = 7),
            GithubUserEntity(id = 8),
            GithubUserEntity(id = 9),
            GithubUserEntity(id = 10))
        )

        assertEquals(10, controller.adapter.itemCount)
    }

    @Test
    fun adapter_with10DataAndLoadingMore() {
        controller.setData(true, true, listOf(
            GithubUserEntity(id = 1),
            GithubUserEntity(id = 2),
            GithubUserEntity(id = 3),
            GithubUserEntity(id = 4),
            GithubUserEntity(id = 5),
            GithubUserEntity(id = 6),
            GithubUserEntity(id = 7),
            GithubUserEntity(id = 8),
            GithubUserEntity(id = 9),
            GithubUserEntity(id = 10))
        )

        assertEquals(11, controller.adapter.itemCount)
    }
}