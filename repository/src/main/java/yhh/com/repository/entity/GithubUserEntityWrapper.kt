package yhh.com.repository.entity

import com.google.gson.annotations.SerializedName

data class GithubUserEntityWrapper (
    @field: [SerializedName("total_count")]
    val totalCount: Int = 0,

    /**
     * To keep the Search API fast for everyone, we limit how long any individual query can run. For queries that exceed the time limit, the API returns the matches that were already found prior to the timeout, and the response has the incomplete_results property set to true.
     */
    @field: [SerializedName("incomplete_results")]
    val incompleteResults: Boolean = false,

    @field: [SerializedName("items")]
    val githubUsers: ArrayList<GithubUserEntity> = ArrayList()

)