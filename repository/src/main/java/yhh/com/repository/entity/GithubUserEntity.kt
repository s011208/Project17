package yhh.com.repository.entity

import com.google.gson.annotations.SerializedName

data class GithubUserEntity(
    @field:[SerializedName("login")]
    val userName: String = "",

    @field:[SerializedName("avatar_url")]
    val avatarUrl: String = "",

    @field:[SerializedName("id")]
    val id: Long = -1
)