package jyotti.apexing.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")
val KEY_PLATFORM = stringPreferencesKey("platform")
val KEY_ID = stringPreferencesKey("id")
val KEY_UID = stringPreferencesKey("uid")
val KEY_REFRESH_DATE = longPreferencesKey("refresh_date")
val KEY_IS_RATED = booleanPreferencesKey("is_rated")