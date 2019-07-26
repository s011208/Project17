package yhh.com.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GithubUserEntity(
    @field:[PrimaryKey(autoGenerate = true) ColumnInfo(name = "r_id")]
    val rowId: Long = 0,

    @field:[SerializedName("login") ColumnInfo(name = "login")]
    val userName: String = "",

    @field:[SerializedName("avatar_url") ColumnInfo(name = "avatar_url")]
    val avatarUrl: String = "",

    @field:[SerializedName("id") ColumnInfo(name = "userId")]
    val userId: Long = -1,

    @field:[ColumnInfo(name = "keyword")]
    var keyword: String = "",

    @field:[ColumnInfo(name = "_order")]
    var order: Int = 0
)