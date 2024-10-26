package me.baggi.schedule.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_preferences")

val GROUP_ID_KEY = longPreferencesKey("user_group_id")

suspend fun saveGroupId(context: Context, groupId: Long) {
    context.dataStore.edit { preferences ->
        preferences[GROUP_ID_KEY] = groupId
    }
}

fun getGroupId(context: Context): Flow<Long?> {
    return context.dataStore.data
        .map { preferences ->
            preferences[GROUP_ID_KEY]
        }
}