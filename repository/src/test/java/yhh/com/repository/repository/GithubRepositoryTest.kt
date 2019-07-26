package yhh.com.repository.repository

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import yhh.com.repository.dao.GithubUserDao
import yhh.com.repository.database.AppDatabase
import yhh.com.repository.util.RxImmediateSchedulerRule

class GithubRepositoryTest {
    @Rule
    @JvmField
    val scheduler = RxImmediateSchedulerRule()

    private lateinit var repository: GithubRepository

    @MockK
    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = GithubRepository(database)
    }
}