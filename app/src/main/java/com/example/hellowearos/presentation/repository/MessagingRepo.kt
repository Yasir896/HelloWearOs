package com.example.hellowearos.presentation.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hellowearos.presentation.model.Contact
import com.example.hellowearos.presentation.model.Contact.Companion.toContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "contacts")

class MessagingRepo(private val context: Context) {

    fun getFavoriteContacts(): Flow<List<Contact>> = context.dataStore.data.map { preferences ->
        val count = preferences[intPreferencesKey("contact.count")] ?: 0

        (0 until count).mapNotNull {
            preferences[stringPreferencesKey("contact.$it")]?.toContact()
        }
    }

    suspend fun updateContacts(contacts: List<Contact>) {
        context.dataStore.edit {
            it.clear()
            contacts.forEachIndexed { index, contact ->
                it[stringPreferencesKey("contact.$index")] = contact.toPrefrenceString()
            }
            it[intPreferencesKey("contact.count")] = contacts.size
        }
    }

    companion object {
        private const val avatarPath =
            "https://github.com/android/wear-os-samples/raw/main/WearTilesKotlin/" +
                    "app/src/main/res/drawable-nodpi"

        val knownContacts = listOf(
            Contact(
                id = 0, initials = "JV", name = "Jyoti V", avatarUrl = null
            ),
            Contact(
                id = 1, initials = "AC", name = "Ali C", avatarUrl = "$avatarPath/ali.png"
            ),
            Contact(
                id = 2, initials = "TB", name = "Taylor B", avatarUrl = "$avatarPath/taylor.jpg"
            ),
            Contact(
                id = 3, initials = "FS", name = "Felipe S", avatarUrl = null
            ),
            Contact(
                id = 4, initials = "JG", name = "Judith G", avatarUrl = null
            ),
            Contact(
                id = 5, initials = "AO", name = "Andrew O", avatarUrl = null
            ),
        )
    }
}