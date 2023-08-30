package com.zeoharlem.testaapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.zeoharlem.testaapp.models.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDataStore @Inject constructor(@ApplicationContext context: Context) : SharePrefManager(context) {

    companion object {
        private const val KEY_FIRST_TESTA_ID = "agent_id"
        private const val KEY_FIRST_TESTA_EMAIL = "agent_email"
        private const val KEY_FIRST_TESTA_VERIFICATION = "agent_verification"
        private const val KEY_USER_AVAILABLE_FOR_WORK = "user_available"
        private const val KEY_FIRST_TESTA_ACCOUNT_ACTIVATED = "agent_activated"

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

    fun saveToken(value: String) {
        saveStringData(KEY_TOKEN, value)
    }

    fun saveUserData(userData: UserData?) {
        userData?.let {
            saveStringData("userData", Gson().toJson(it))
        }
    }

    fun getToken(): String {
        return getStringData(KEY_TOKEN)
    }

    fun getUserData(): String {
        return getStringData("userData")
    }

    suspend fun userIsLoggedIn(): Boolean {
        return getStringData(KEY_TOKEN).isNotEmpty()
    }
}