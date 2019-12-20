package com.yuyakaido.android.gaia.auth

import android.app.Application
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.yuyakaido.android.gaia.core.domain.app.AuthTokenServiceType
import com.yuyakaido.android.gaia.core.domain.value.AuthToken

class AuthTokenService(
  override val application: Application
) : AuthTokenServiceType {

  companion object {
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
  }

  private val preference = PreferenceManager.getDefaultSharedPreferences(application)

  override fun current(): AuthToken {
    return AuthToken(
      accessToken = preference.getString(ACCESS_TOKEN, null),
      refreshToken = preference.getString(REFRESH_TOKEN, null)
    )
  }

  override fun save(token: AuthToken) {
    preference.edit(commit = false) {
      putString(ACCESS_TOKEN, token.accessToken())
      putString(REFRESH_TOKEN, token.refreshToken())
    }
  }

  override fun delete() {
    preference.edit(commit = false) {
      putString(ACCESS_TOKEN, null)
      putString(REFRESH_TOKEN, null)
    }
  }

}