package yhh.com.repository.repository

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import yhh.com.repository.util.RxImmediateSchedulerRule

class GithubRepositoryTest {
    @Rule
    @JvmField
    val scheduler = RxImmediateSchedulerRule()

    private lateinit var repository: GithubRepository

    @Before
    fun setup() {
        repository = GithubRepository()
    }

    @Test
    fun searchUsers() {
        repository.searchUsers("s011208")
            .test().assertValue {
                it.totalCount == 1 && it.githubUsers.size == 1 && it.githubUsers[0].userName == "s011208"
            }
    }
}