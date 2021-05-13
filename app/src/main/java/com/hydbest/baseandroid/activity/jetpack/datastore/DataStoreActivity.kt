package com.hydbest.baseandroid.activity.jetpack.datastore

import androidx.appcompat.app.AppCompatActivity

/*import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hydbest.baseandroid.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences*/

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreActivity: AppCompatActivity() {
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_store)
    }

    //字段的key，需要使用stringPreferencesKey包装一下
    private val nameKey = stringPreferencesKey("name")

    suspend fun putName(name: String) {
       dataStore.edit { setting ->
            setting[nameKey] = name
        }
    }

    suspend fun getName(): String {
        val nameFlow: Flow<String> = dataStore.data.map { settings ->
            settings[nameKey] ?: ""
        }
        val text = nameFlow.first()
        findViewById<TextView>(R.id.tv).text = text
        return text
    }*/

}