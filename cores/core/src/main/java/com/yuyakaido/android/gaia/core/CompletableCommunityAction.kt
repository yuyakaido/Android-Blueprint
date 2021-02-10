package com.yuyakaido.android.gaia.core

import com.yuyakaido.android.reduxkit.CompletableActionType
import com.yuyakaido.android.reduxkit.SelectorType

interface CompletableCommunityAction : CompletableActionType<AppState, CommunityState> {
  override fun selector(root: SelectorType<AppState>): SelectorType<CommunityState> {
    return object : SelectorType<CommunityState> {
      override fun select(): CommunityState {
        return root.select().community
      }
    }
  }
}