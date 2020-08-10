package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.MockConfiguration
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.network.etsy.EtsyApiService
import com.google.common.truth.Truth.assertWithMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import app.mblackman.orderfulfillment.data.database.User as DatabaseUser
import app.mblackman.orderfulfillment.data.network.etsy.json.User as EtsyUser

class UserRepositoryImplTest {

    private val etsyApiService: EtsyApiService = mockk()
    private val database: StoreDatabase = mockk(relaxed = true)
    private val configuration = MockConfiguration()

    @After
    fun after() {
        unmockkObject(etsyApiService)
        unmockkObject(database)
        configuration.currentUserId = null
    }

    @Test
    fun getUserFromDatabaseMatchConfiguration() {
        val dbUser = DatabaseUser(1)
        val etsyUser = createEtsyUser(2)
        val repo = setupMocks(dbUser.id, dbUser, etsyUser)

        val result = runBlocking { repo.getCurrentUserAsync() }

        assertWithMessage("The user should have come from the database since it matches the config's user id.")
            .that(result?.id).isEqualTo(dbUser.id)
    }

    @Test
    fun getUserFromDatabaseNoMatchConfiguration() {
        val etsyUser = createEtsyUser(2)
        val repo = setupMocks(100, null, etsyUser)

        val result = runBlocking { repo.getCurrentUserAsync() }

        assertWithMessage("The Etsy API should be called since the config user id didn't match a database user.")
            .that(result?.id).isEqualTo(etsyUser.id)
        assertWithMessage("The configuration's user should have been set by to the retrieved user.")
            .that(configuration.currentUserId).isEqualTo(etsyUser.id)
    }

    @Test
    fun getNoUser() {
        val repo = setupMocks(100, null, null)

        val result = runBlocking { repo.getCurrentUserAsync() }

        assertWithMessage("No user should be retrieved.")
            .that(result).isNull()
    }

    private fun setupMocks(
        configurationUserId: Int?,
        dbUser: DatabaseUser?,
        etsyUser: EtsyUser?
    ): UserRepository {
        configuration.currentUserId = configurationUserId

        if (dbUser != null) {
            every { database.userDao.findUserById(dbUser.id) } returns dbUser
        } else {
            every { database.userDao.findUserById(any()) } returns null
        }

        etsyUser?.let {
            every { etsyApiService.getUserSelfAsync() } returns CompletableDeferred(listOf(it).toEtsyResponse())
        }

        return UserRepositoryImpl(
            etsyApiService,
            database,
            configuration,
            DatabaseToDomainUserMapper(),
            EtsyToDomainUserMapper()
        )
    }

    private fun createEtsyUser(id: Int): EtsyUser {
        return EtsyUser(
            id,
            "Test",
            "TestEmail",
            Float.MIN_VALUE,
            Int.MIN_VALUE,
            Int.MIN_VALUE,
            false
        )
    }
}