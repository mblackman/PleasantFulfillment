package app.mblackman.orderfulfillment.data.repository

import app.mblackman.orderfulfillment.data.database.User as DatabaseUser
import app.mblackman.orderfulfillment.data.domain.User as DomainUser
import app.mblackman.orderfulfillment.data.network.json.User as EtsyUser

class DatabaseToDomainUserMapper
    : Mapper<DatabaseUser, DomainUser> {
    override fun map(input: DatabaseUser): DomainUser {
        return DomainUser(input.id)
    }

}

class EtsyToDomainUserMapper
    : Mapper<EtsyUser, DomainUser> {
    override fun map(input: EtsyUser): DomainUser {
        return DomainUser(input.id)
    }

}