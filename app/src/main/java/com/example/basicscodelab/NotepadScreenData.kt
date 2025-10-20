package com.example.basicscodelab

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore(
    name = "notepad_settings"
)
class NotepadScreenData(private val context: Context) {
    companion object {
        val NOTEPAD_TEXT_KEY = stringPreferencesKey("notepad_text_content")
    }

    // --- Read Data ---
    val notepadTextFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[NOTEPAD_TEXT_KEY] ?: ""
        }

    // --- Write Data ---
    suspend fun saveNotepadText(text: String) {
        context.dataStore.edit { preferences ->
            preferences[NOTEPAD_TEXT_KEY] = text
        }
    }
}