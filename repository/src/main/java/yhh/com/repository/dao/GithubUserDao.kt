package yhh.com.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single
import yhh.com.repository.entity.GithubUserEntity

@Dao
interface GithubUserDao {

    @Insert
    fun insert(entity: GithubUserEntity)

    @Insert
    fun insertAll(entities: List<GithubUserEntity>)

    @Query("SELECT * FROM `GithubUserEntity` WHERE `keyword` = :keyword order by `_order`")
    fun getAll(keyword: String): Single<List<GithubUserEntity>>

    @Query("SELECT * FROM `GithubUserEntity` WHERE `keyword` = :keyword AND `_order` BETWEEN :start AND :end order by `_order`")
    fun getRange(start: Int, end: Int, keyword: String): Single<List<GithubUserEntity>>
}