package yhh.com.repository.repository

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import yhh.com.repository.entity.GithubUserEntityWrapper

class GithubRepository {

    private interface GithubApi {

        @GET("/search/users")
        fun searchUsers(
            @Query("q") keyword: String,
            @Query("page") page: Int,
            @Query("per_page") itemPerPage: Int
        ): Single<GithubUserEntityWrapper>
    }

    private val githubApi =
        Retrofit
            .Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubApi::class.java)

    fun searchUsers(keyword: String, page: Int = 1, itemPerPage: Int = 20) =
        githubApi.searchUsers(keyword, page, itemPerPage)
}