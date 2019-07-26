package yhh.com.project17.activity.mvrx.viewmodel

import android.os.Build
import androidx.lifecycle.Lifecycle
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.UniqueOnly
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import yhh.com.project17.util.TestLifecycleOwner
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
    fun getPageCount_blankKeyword_setToUninitialized() {
        viewModel = spyk(MainFragmentViewModel(MainFragmentState(pageNumberStateAsync = Success(0)), repository))

        viewModel.getPageCount("")

        viewModel.subscribe(owner, UniqueOnly("test")) {
            assert(it.pageNumberStateAsync is Uninitialized)
        }
    }

    @Test
    fun getPageCount() {
        viewModel = spyk(MainFragmentViewModel(MainFragmentState(pageNumberStateAsync = Success(0)), repository))

        every { repository.getPageCount("s011208", 50) } returns Single.fromCallable { 9987 }

        viewModel.getPageCount("s011208")

        verify(exactly = 1) { repository.getPageCount("s011208", 50) }

        viewModel.subscribe(owner, UniqueOnly("test")) {
            assert(it.pageNumberStateAsync is Success && it.pageNumberStateAsync.invoke() == 9987)
        }
    }
}