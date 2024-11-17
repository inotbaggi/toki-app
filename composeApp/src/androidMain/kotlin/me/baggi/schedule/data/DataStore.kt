package me.baggi.schedule.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStore {
    var metricParams = mutableMapOf<String, String>()
    var lessonPeriods = mapOf<Long, LessonTime>()
    var cachedWeek = mutableMapOf<Long, ScheduleDayDTO>()

    lateinit var appInfo: AppInfo
}

val Context.dataStore by preferencesDataStore("preferences")

val GROUP_ID_KEY = longPreferencesKey("user_group_id")
val IS_TEACHER = stringPreferencesKey("is_teacher")

class ConfigManager(private val context: Context) {
    val groupId: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[GROUP_ID_KEY]
    }
    val isTeacher: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[IS_TEACHER]
    }

    suspend fun setGroupId(groupId: Long) {
        context.dataStore.edit { preferences ->
            preferences[GROUP_ID_KEY] = groupId
        }
    }
    suspend fun setIsTeacher(isTeacher: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_TEACHER] = isTeacher
        }
    }
}