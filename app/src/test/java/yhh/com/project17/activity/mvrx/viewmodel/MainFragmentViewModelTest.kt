package yhh.com.project17.activity.mvrx.viewmodel

import android.os.Build
import androidx.lifecycle.Lifecycle
import com.airbnb.mvrx.DeliveryMode
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.UniqueOnly
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import yhh.com.project17.util.TestLifecycleOwner
import yhh.com.repository.entity.GithubUserEntityWrapper
import yhh.com.repository.repository.GithubRepository
import yhh.com.repository.util.RxImmediateSchedulerRule

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O], manifest = Config.NONE)
class MainFragmentViewModelTest {

    @Rule
    @JvmField
    val scheduler = RxImmediateSchedulerRule()

    @MockK
    internal lateinit var repository: GithubRepository

    private lateinit var viewModel: MainFragmentViewModel

    private lateinit var owner: TestLifecycleOwner

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        owner = TestLifecycleOwner()
        owner.lifecycle.markState(Lifecycle.State.STARTED)
    }

    @Test
    fun search() {
    }

    @Test
    fun loadMore_totalCountIs0_doNotSearch() {
        viewModel = spyk(
            MainFragmentViewModel(
                MainFragmentState(
                    githubUserEntityWrapperAsync = Success(GithubUserEntityWrapper()),
                    totalCount = 0,
                    keyword = "123",
                    page = 10,
                    isLoadingMore = true
                ), repository
            )
        )
        viewModel.loadMore()

        viewModel.subscribe(owner = owner, subscriber = {
            System.out.println(it)
            assert(it.keyword == "123" && it.totalCount == 0 && !it.isLoadingMore)
        }, deliveryMode = UniqueOnly("test"))
    }

    @Test
    fun loadMore_search() {
        viewModel =
            spyk(
                MainFragmentViewModel(
                    MainFragmentState(
                        githubUserEntityWrapperAsync = Success(GithubUserEntityWrapper()),
                        totalCount = 1000,
                        keyword = "123",
                        page = 1
                    ), repository
                )
            )
        every { viewModel.search("123", 2, 30, true) } just Runs

        viewModel.loadMore()

        verify(exactly = 1) { viewModel.search("123", 2, 30, true) }
    }
}