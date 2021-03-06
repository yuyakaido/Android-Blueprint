package com.yuyakaido.android.gaia.community

import com.yuyakaido.android.gaia.core.domain.entity.Community
import com.yuyakaido.android.gaia.core.domain.repository.CommunityRepositoryType
import com.yuyakaido.android.gaia.core.domain.value.EntityPaginationItem

class CommunityRepository(
  private val api: CommunityApi
) : CommunityRepositoryType {

  override suspend fun mine(
    after: String?
  ): EntityPaginationItem<Community.Detail> {
    return api.communitiesOfMine(after = after).toCommunityPaginationItem()
  }

  override suspend fun detail(community: Community.Summary): Community.Detail {
    return api.detail(community = community.name.value).toEntity()
  }

  override suspend fun subscribe(community: Community.Detail): Community.Detail {
    api.subscribe(name = community.name.value)
    return community.copy(isSubscriber = true)
  }

  override suspend fun unsubscribe(community: Community.Detail): Community.Detail {
    api.unsubscribe(name = community.name.value)
    return community.copy(isSubscriber = false)
  }

}