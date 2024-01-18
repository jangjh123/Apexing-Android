package jyotti.apexing.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")
val DATASTORE_KEY_ID = stringPreferencesKey("id")
val DATASTORE_KEY_UID = stringPreferencesKey("uid")
val KEY_IS_RATED = booleanPreferencesKey("is_rated")