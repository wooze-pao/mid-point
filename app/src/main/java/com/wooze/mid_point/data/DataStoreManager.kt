package com.wooze.mid_point.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Setting")

    val TILE_AUTO_COLLAPSE = booleanPreferencesKey("tile_collapse")
    val isAutoCollapse: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[TILE_AUTO_COLLAPSE] ?: true
    }

    suspend fun setAutoCollapse(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TILE_AUTO_COLLAPSE] = enabled
        }
    }
}
