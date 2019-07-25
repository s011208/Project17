package yhh.com.project17.activity.mvrx.viewmodel

import com.airbnb.mvrx.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import yhh.com.project17.activity.MainFragment
import yhh.com.project17.external.mvrx.MvRxViewModel
import yhh.com.repository.entity.GithubUserEntity
import yhh.com.repository.entity.GithubUserEntityWrapper
import yhh.com.repository.repository.GithubRepository
import java.util.concurrent.TimeUnit

data class MainFragmentState(
    val keyword: String = "",
    val page: Int = 1,
    val isLoadingMore: Boolean = false,
    val githubUserEntityWrapperAsync: Async<GithubUserEntityWrapper> = Uninitialized,
    val totalCount: Int = 0,
    val users: List<GithubUserEntity> = listOf()
) : MvRxState

class MainFragmentViewModel @AssistedInject constructor(
    @Assisted initialState: MainFragmentState,
    private val githubRepository: GithubRepository
) : MvRxViewModel<MainFragmentState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: MainFragmentState): MainFragmentViewModel
    }

    companion object : MvRxViewModelFactory<MainFragmentViewModel, MainFragmentState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: MainFragmentState
        ): MainFragmentViewModel? {
            return (viewModelContext as FragmentViewModelContext).fragment<MainFragment>()
                .viewModelFactory.create(state)
        }

        private const val NUMBER_OF_ITEM_PER_PAGE = 30
    }

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun search(keyword: String, page: Int = 1) {
        withState { currentState ->
            if (currentState.githubUserEntityWrapperAsync is Loading) {
                Timber.v("force cancel ${currentState.keyword}")
                disposable?.dispose()
            }

            if (keyword.isBlank()) {
                setState {
                    copy(
                        githubUserEntityWrapperAsync = Uninitialized,
                        keyword = keyword,
                        page = page,
                        isLoadingMore = false,
                        users = ArrayList(),
                        totalCount = 0
                    )
                }
                return@withState
            }
            search(keyword, page, NUMBER_OF_ITEM_PER_PAGE)
        }
    }

    private fun search(keyword: String, page: Int, itemsPerPage: Int, isLoadingMore: Boolean = false) {
        if (!isLoadingMore) {
            setState { copy(users = listOf(), totalCount = 0) }
        }

        disposable = githubRepository.searchUsers(keyword, page, itemsPerPage)
            .timeout(10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .doOnError { Timber.w(it, "failed to search users") }
            .execute {
                val list: List<GithubUserEntity>
                val totalCount: Int
                if (it is Success && it.invoke() != null) {
                    list = this.users + it.invoke()!!.githubUsers
                    totalCount = it.invoke()!!.totalCount
                } else {
                    list = this.users
                    totalCount = this.totalCount
                }
                return@execute copy(
                    githubUserEntityWrapperAsync = it,
                    keyword = keyword,
                    page = if (it is Fail) page - 1 else page,
                    isLoadingMore = if (it is Success || it is Fail || it is Uninitialized) false else isLoadingMore,
                    users = list,
                    totalCount = totalCount
                )
            }
    }

    fun loadMore() {
        withState {
            Timber.v("loadMore, ${it.totalCount}")
            if (it.totalCount > it.page * NUMBER_OF_ITEM_PER_PAGE) {
                search(it.keyword, it.page + 1, NUMBER_OF_ITEM_PER_PAGE, true)
            } else {
                setState { copy(isLoadingMore = false) }
            }
        }
    }
}