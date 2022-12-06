package com.litmethod.android.utlis

import android.content.Context
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataPreferenceObject(context: Context) {

    val  dataStore = context.createDataStore(name = "settings2")


    suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value

        }

    }
    suspend fun deleteAllData() {

        dataStore.edit {
            it.clear()
        }

    }

    val dataStoreKey = preferencesKey<String>("theUserDiplayName")
    val userNameFlow: Flow<String> = dataStore.data.map {
        it[dataStoreKey] ?: ""
    }

    fun getTheData(key: String): Flow<String> {
        val dataStoreKey = preferencesKey<String>(key)
        val userNameFlow: Flow<String> = dataStore.data.map {
            it[dataStoreKey] ?: ""
        }
        return userNameFlow
    }
}
