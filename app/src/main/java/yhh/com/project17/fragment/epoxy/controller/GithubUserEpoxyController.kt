package yhh.com.project17.fragment.epoxy.controller

import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import yhh.com.project17.fragment.epoxy.model.emptyResult
import yhh.com.project17.fragment.epoxy.model.githubUser
import yhh.com.repository.entity.GithubUserEntity

class GithubUserEpoxyController : TypedEpoxyController<List<GithubUserEntity>>() {

    internal val reloadIntent = PublishSubject.create<Unit>()

    override fun buildModels(githubUsers: List<GithubUserEntity>) {
        Timber.v("user list size: ${githubUsers.size}")

        if (githubUsers.isEmpty()) {
            emptyResult {
                id("emptyResult")
                reloadIntent(reloadIntent)
            }
        } else {
            githubUsers.forEach { userInfo ->
                githubUser {
                    userName(userInfo.userName)
                    avatarUrl(userInfo.avatarUrl)
                    id(userInfo.userId)
                }
            }
        }
    }
}