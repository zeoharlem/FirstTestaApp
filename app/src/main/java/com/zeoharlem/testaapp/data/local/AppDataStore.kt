package com.zeoharlem.testaapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.zeoharlem.testaapp.models.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDataStore @Inject constructor(@ApplicationContext context: Context) : CacheData(context) {

    companion object {
        const val KEY_FIRST_TESTA_ID = "agent_id"
        const val KEY_FIRST_TESTA_EMAIL = "agent_email"
        const val KEY_FIRST_TESTA_VERIFICATION = "agent_verification"
        const val KEY_USER_AVAILABLE_FOR_WORK = "user_available"
        const val KEY_FIRST_TESTA_ACCOUNT_ACTIVATED = "agent_activated"

        const val KEY_FIRST_TESTA_ONBOARDED = "agent_onboarded"
        const val KEY_TOKEN = "agent_token"


        private fun loginDataKeys(): List<String> {
            return listOf(
                KEY_FIRST_TESTA_ID,
                KEY_FIRST_TESTA_EMAIL,
                KEY_FIRST_TESTA_ACCOUNT_ACTIVATED,
                KEY_FIRST_TESTA_VERIFICATION,
                KEY_USER_AVAILABLE_FOR_WORK
            )
        }
    }

    suspend fun saveToken(value: String) {
        saveStringData(KEY_TOKEN, value)
    }

    suspend fun saveUserData(userData: UserData?) {
        userData?.let {
            saveStringData("userData", Gson().toJson(it))
        }
    }

    fun getToken(): Flow<String?> {
        return getDataAsFlow(KEY_TOKEN)
    }

    fun getUserData(): Flow<String?> {
        return getDataAsFlow("userData")
    }

    suspend fun userIsLoggedIn(): Boolean {
        return getStringData(KEY_TOKEN)?.isNotEmpty() == true
    }

    suspend fun deleteKeys() {
        loginDataKeys().forEach {
            val key = stringPreferencesKey(it)
            clearData(key)
        }
    }
}