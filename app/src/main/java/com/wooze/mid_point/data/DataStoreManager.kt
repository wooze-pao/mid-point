package com.wooze.mid_point.data

import android.R.attr.enabled
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Setting")

class DataStoreManager(val context: Context) {

    private fun <T> getKeyFlow(key: Preferences.Key<T>, default: T): Flow<T> {
        return context.dataStore.data.map { preferences ->
            preferences[key] ?: default
        }
    }

    private suspend fun <T> setKeyValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    val TILE_AUTO_COLLAPSE = booleanPreferencesKey("tile_collapse")
    val isAutoCollapse: Flow<Boolean> = getKeyFlow(TILE_AUTO_COLLAPSE, true)

    suspend fun setAutoCollapse(enabled: Boolean) {
        setKeyValue(TILE_AUTO_COLLAPSE,enabled)
    }

}
