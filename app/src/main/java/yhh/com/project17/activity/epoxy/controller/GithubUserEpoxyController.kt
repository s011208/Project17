package yhh.com.project17.activity.epoxy.controller

import com.airbnb.epoxy.Typed3EpoxyController
import timber.log.Timber
import yhh.com.project17.activity.epoxy.model.emptyResult
import yhh.com.project17.activity.epoxy.model.githubUser
import yhh.com.project17.activity.epoxy.model.progressBar
import yhh.com.repository.entity.GithubUserEntity

class GithubUserEpoxyController : Typed3EpoxyController<Boolean, Boolean, List<GithubUserEntity>>() {

    override fun buildModels(initialized: Boolean, isLoadingMore: Boolean, githubUsers: List<GithubUserEntity>) {
        Timber.v("user list size: ${githubUsers.size}, initialized: $initialized, isLoadingMore: $isLoadingMore")

        if (githubUsers.isEmpty()) {
            if (initialized) {
                emptyResult {
                    id("emptyResult")
                }
            }
        } else {
            githubUsers.forEach { userInfo ->
                githubUser {
                    userName(userInfo.userName)
                    avatarUrl(userInfo.avatarUrl)
                    id(userInfo.id)
                }
            }

            if (isLoadingMore) {
                progressBar {
                    id("progressBar")
                }
            }
        }
    }
}