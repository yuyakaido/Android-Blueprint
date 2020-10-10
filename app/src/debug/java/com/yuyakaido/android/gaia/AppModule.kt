package com.yuyakaido.android.gaia

import android.app.Application
import com.yuyakaido.android.gaia.core.AppStore
import com.yuyakaido.android.gaia.core.domain.app.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule : MainAppModule() {

  @AppScope
  @Provides
  fun provideSupportNotificationManager(
    application: Application,
    appStore: AppStore
  ): SupportNotificationManager {
    return DebugSupportNotificationManager(
      application = application,
      appStore = appStore
    )
  }

}