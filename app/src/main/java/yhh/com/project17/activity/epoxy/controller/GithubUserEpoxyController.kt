package yhh.com.project17.activity.epoxy.controller

import com.airbnb.epoxy.Typed2EpoxyController
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import yhh.com.project17.activity.epoxy.model.emptyResult
import yhh.com.project17.activity.epoxy.model.githubUser
import yhh.com.project17.activity.epoxy.model.progressBar
import yhh.com.repository.entity.GithubUserEntityWrapper

class GithubUserEpoxyController : Typed2EpoxyController<Boolean, GithubUserEntityWrapper>() {

    internal val onLoadMoreIntent = PublishSubject.create<Unit>()

    override fun buildModels(initialized: Boolean, wrapper: GithubUserEntityWrapper) {
        if (initialized) {
            Timber.v("user list size: ${wrapper.githubUsers.size}")

            if (wrapper.githubUsers.isEmpty()) {
                emptyResult {
                    id("emptyResult")
                }
            } else {

                wrapper.githubUsers.forEach { userInfo ->
                    githubUser {
                        userName(userInfo.userName)
                        avatarUrl(userInfo.avatarUrl)
                        id(userInfo.id)
                    }
                }

                if (wrapper.totalCount > wrapper.githubUsers.size) {
                    progressBar {
                        id("ProgressBarEpoxyModel_")
                        loadMoreIntent(onLoadMoreIntent)
                    }
                }
            }
        }
    }
}