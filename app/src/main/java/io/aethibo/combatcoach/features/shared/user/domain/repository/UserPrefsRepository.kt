package io.aethibo.combatcoach.features.shared.user.domain.repository

import io.aethibo.combatcoach.features.shared.user.data.exceptions.UserPrefsException

// The interface gains @Throws documentation
interface UserPrefsRepository {
    @Throws(UserPrefsException.LoadException::class)
    suspend fun getString(key: String, default: String = ""): String

    @Throws(UserPrefsException.LoadException::class)
    suspend fun getBoolean(key: String, default: Boolean = false): Boolean

    @Throws(UserPrefsException.LoadException::class)
    suspend fun getInt(key: String, default: Int = 0): Int

    @Throws(UserPrefsException.SaveException::class)
    suspend fun setString(key: String, value: String)

    @Throws(UserPrefsException.SaveException::class)
    suspend fun setBoolean(key: String, value: Boolean)

    @Throws(UserPrefsException.SaveException::class)
    suspend fun setInt(key: String, value: Int)

    suspend fun delete(key: String)
}
