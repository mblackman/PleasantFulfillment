package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.Configuration
import app.mblackman.orderfulfillment.data.database.StoreDatabase
import app.mblackman.orderfulfillment.data.network.EtsyApiService
import app.mblackman.orderfulfillment.data.database.User as DatabaseUser
import app.mblackman.orderfulfillment.data.domain.User as DomainUser
import app.mblackman.orderfulfillment.data.network.json.User as EtsyUser

/**
 * Implementation of UserRepository. Gets user data from various data sources.
 *
 * @param etsyApiService The service to get live Etsy data from.
 * @param storeDatabase The database to retrieve and store data.
 * @param configuration Application configuration information.
 * @param dbToDomainUserMapper Maps database users to domain users.
 * @param etsyToDomainUserMapper Maps Etsy users to domain users.
 */
class UserRepositoryImpl(
    private val etsyApiService: EtsyApiService,
    private val storeDatabase: StoreDatabase,
    private val configuration: Configuration,
    private val dbToDomainUserMapper: Mapper<DatabaseUser, DomainUser>,
    private val etsyToDomainUserMapper: Mapper<EtsyUser, DomainUser>
) : UserRepository() {

    /**
     * Gets the user for the logged in user to the application.
     *
     * @return The user for the currently logged in user.
     */
    override suspend fun getCurrentUserAsync(): DomainUser? {
        configuration.currentUserId?.let {
            storeDatabase.userDao.findUserById(it)?.let { dbUser ->
                return dbToDomainUserMapper.map(dbUser)
            }
        }

        val selfUser = safeApiCall(
            call = { _, _ -> etsyApiService.getUserSelfAsync().await() },
            error = "Failed to get self user."
        )

        if (selfUser?.size == 1) {
            val user = selfUser[0]
            configuration.currentUserId = user.id
            return etsyToDomainUserMapper.map(user)
        }

        return null
    }
}