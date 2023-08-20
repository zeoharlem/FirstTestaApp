package com.zeoharlem.testaapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CacheData @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: DataStore<Preferences>
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("FirstTesta_Data")
    }

    init {
        prefs = context.dataStore
    }

    fun getDataAsFlow(key: String): Flow<String?> = prefs.data.map { preferences ->
        preferences.get(stringPreferencesKey(key))
    }

    suspend fun saveData(key: String, value: Any) {
        when (value) {
            is String -> saveStringData(key, value)
            is Boolean -> saveBooleanData(key, value)
            is Long -> saveLongData(key, value)
            is Int -> saveIntData(key, value)
        }
    }

    suspend fun saveStringData(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        prefs.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        prefs.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun saveIntData(key: String, value: Int) {
        val dataStoreKey = intPreferencesKey(key)
        prefs.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun saveLongData(key: String, value: Long) {
        val dataStoreKey = longPreferencesKey(key)
        prefs.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun getStringData(key: String): String? {
        val preferences = prefs.data.first()
        return preferences[stringPreferencesKey(key)]
    }

    suspend fun getIntData(key: String): Int? {
        val preferences = prefs.data.first()
        return preferences[intPreferencesKey(key)]
    }

    suspend fun getLongData(key: String): Long? {
        val preferences = prefs.data.first()
        return preferences[longPreferencesKey(key)]
    }

    suspend fun getBooleanData(key: String): Boolean? {
        val preferences = prefs.data.first()
        return preferences[booleanPreferencesKey(key)]
    }

    fun isKeyStored(key: Preferences.Key<String>): Flow<Boolean> = prefs.data.map { preference ->
        preference.contains(key)
    }

    suspend fun clearData(key: Preferences.Key<String>) = prefs.edit {
        //it.remove(key)
    }
}