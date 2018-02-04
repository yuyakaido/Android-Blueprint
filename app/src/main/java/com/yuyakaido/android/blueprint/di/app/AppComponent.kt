package com.yuyakaido.android.blueprint.di.app

import com.yuyakaido.android.blueprint.di.account.AccountComponent
import com.yuyakaido.android.blueprint.di.account.AccountModule
import com.yuyakaido.android.blueprint.presentation.AccountListActivity
import com.yuyakaido.android.blueprint.presentation.TimelineActivity
import dagger.Component

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: TimelineActivity)
    fun inject(activity: AccountListActivity)

    fun newAccountComponent(module: AccountModule): AccountComponent
}