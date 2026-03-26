package io.aethibo.combatcoach.shared.user.data.repository

import io.aethibo.combatcoach.shared.user.data.dao.UserPrefsDao
import io.aethibo.combatcoach.shared.user.data.dao.entity.UserPrefsEntity
import io.aethibo.combatcoach.shared.user.data.exceptions.UserPrefsException
import io.aethibo.combatcoach.shared.user.domain.repository.UserPrefsRepository

class UserPrefsRepositoryImpl(
    private val dao: UserPrefsDao,
) : UserPrefsRepository {

    override suspend fun getString(key: String, default: String): String =
        runCatching { dao.get(key) ?: default }
            .getOrElse { throw UserPrefsException.LoadException() }

    override suspend fun getBoolean(key: String, default: Boolean): Boolean =
        runCatching { dao.get(key)?.toBooleanStrictOrNull() ?: default }
            .getOrElse { throw UserPrefsException.LoadException() }

    override suspend fun getInt(key: String, default: Int): Int =
        runCatching { dao.get(key)?.toIntOrNull() ?: default }
            .getOrElse { throw UserPrefsException.LoadException() }

    override suspend fun setString(key: String, value: String) =
        runCatching { dao.set(UserPrefsEntity(key, value)) }
            .getOrElse { throw UserPrefsException.SaveException() }

    override suspend fun setBoolean(key: String, value: Boolean) =
        runCatching { dao.set(UserPrefsEntity(key, value.toString())) }
            .getOrElse { throw UserPrefsException.SaveException() }

    override suspend fun setInt(key: String, value: Int) =
        runCatching { dao.set(UserPrefsEntity(key, value.toString())) }
            .getOrElse { throw UserPrefsException.SaveException() }

    override suspend fun delete(key: String) = dao.delete(key)
}
