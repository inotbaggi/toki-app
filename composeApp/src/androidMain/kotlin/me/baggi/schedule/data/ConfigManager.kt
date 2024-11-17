package me.baggi.schedule.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("preferences")

class ConfigManager(private val context: Context) {
    private val groupIdKey = longPreferencesKey("user_group_id")
    private val isTeacherKey = stringPreferencesKey("is_teacher")

    val groupId: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[groupIdKey]
    }
    val isTeacher: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[isTeacherKey]
    }

    suspend fun setGroupId(groupId: Long) {
        context.dataStore.edit { preferences ->
            preferences[groupIdKey] = groupId
        }
    }
    suspend fun setIsTeacher(isTeacher: String) {
        context.dataStore.edit { preferences ->
            preferences[isTeacherKey] = isTeacher
        }
    }
}