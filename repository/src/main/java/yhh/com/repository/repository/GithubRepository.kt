package yhh.com.repository.repository

import android.net.Uri
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import yhh.com.repository.BuildConfig
import yhh.com.repository.database.AppDatabase
import yhh.com.repository.entity.GithubUserEntity
import yhh.com.repository.entity.GithubUserEntityWrapper
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GithubRepository @Inject constructor(private val database: AppDatabase) {

    private interface GithubApi {

        @GET("/search/users")
        fun searchUsers(
            @Query("q") keyword: String,
            @Query("page") page: Int,
            @Query("per_page") itemPerPage: Int,
            @Query("client_id") clientId: String = BuildConfig.GITHUB_CLIENT_ID,
            @Query("client_secret") clientSecret: String = BuildConfig.GITHUB_CLIENT_SECRET
        ): Single<GithubUserEntityWrapper>

        @GET("/search/users")
        fun getPageCount(
            @Query("q") keyword: String,
            @Query("per_page") itemPerPage: Int,
            @Query("client_id") clientId: String = BuildConfig.GITHUB_CLIENT_ID,
            @Query("client_secret") clientSecret: String = BuildConfig.GITHUB_CLIENT_SECRET
        ): Single<Response<GithubUserEntityWrapper>>
    }

    private val githubApi =
        Retrofit
            .Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubApi::class.java)

    fun searchUsers(keyword: String, page: Int = 1, itemPerPage: Int = 20): Single<List<GithubUserEntity>> {
        val start = (page - 1) * itemPerPage
        val end = start + itemPerPage - 1
        return Single.concat(
            database.githubUserDao().getRange(start, end, keyword)
                .doOnSuccess { Timber.v("get from local, size: ${it.size}") },
            githubApi.searchUsers(keyword, page, itemPerPage)
                .timeout(10, TimeUnit.SECONDS)
                .map { it.githubUsers }
                .doOnSuccess {
                    Timber.v("get from remote, size: ${it.size}")
                    if (it.isNotEmpty()) {
                        for (index in 0 until it.size) {
                            val entity = it[index]
                            entity.keyword = keyword
                            entity.order = start + index
                        }
                        database.githubUserDao().insertAll(it)
                    }
                }
        )
            .filter { it.isNotEmpty() }
            .firstOrError()
    }

    fun getPageCount(keyword: String, itemPerPage: Int = 20): Single<Int> =
        githubApi.getPageCount(keyword, itemPerPage)
            .timeout(10, TimeUnit.SECONDS)
            .map {
                val header = it.headers()
                val responseBody = it.body()
                val errorBody = it.errorBody()
                Timber.v("response: $responseBody")
                Timber.v("keyword: $keyword, header[\"Link\"]: ${header["Link"]}")
                if (errorBody != null) {
                    Timber.w(errorBody.string())
                    throw Exception(errorBody.string())
                }
                Timber.v("errorBody: $errorBody")
                if (header["Link"].isNullOrBlank()) {
                    if (responseBody == null) return@map 0
                    else {
                        return@map Math.ceil(responseBody.totalCount / itemPerPage.toDouble()).toInt()
                    }
                } else {
                    val links = header["Link"]!!.split(",")
                    links.forEach { link ->
                        if (link.contains("rel=\"last\"", ignoreCase = true)) {
                            var html = link.split(";")[0].trim()
                            html = html.substring(1, html.length - 1)
                            Timber.i("html: $html")
                            return@map (Uri.parse(html).getQueryParameter("page") ?: "").toInt()
                        }
                    }
                    return@map 0
                }
            }
}