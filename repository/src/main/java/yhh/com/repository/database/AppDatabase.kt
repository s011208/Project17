package yhh.com.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import yhh.com.repository.dao.GithubUserDao
import yhh.com.repository.entity.GithubUserEntity


@Database(entities = [GithubUserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao
}