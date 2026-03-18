package io.aethibo.combatcoach.shared.utils

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed interface ItemType {

    @Serializable
    data object Workout : ItemType

    @Serializable
    data object Combo : ItemType

    @Serializable
    data object Plan : ItemType
}

/**
 * Custom [NavType] for [ItemType].
 *
 * Compose Navigation cannot automatically derive a NavType for sealed interfaces,
 * even when they are @Serializable, because the type map is empty by default for
 * non-primitive argument types.  We serialise to/from a JSON string so the value
 * survives process death and deep-link reconstruction.
 */
val ItemTypeNavType = object : NavType<ItemType>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): ItemType? =
        bundle.getString(key)?.let(::parseValue)

    override fun parseValue(value: String): ItemType =
        Json.decodeFromString<ItemType>(value)

    override fun serializeAsValue(value: ItemType): String =
        Json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: ItemType) =
        bundle.putString(key, serializeAsValue(value))
}
