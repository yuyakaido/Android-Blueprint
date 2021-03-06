package com.yuyakaido.android.gaia.community.list

import android.view.View
import com.bumptech.glide.Glide
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.yuyakaido.android.gaia.community.R
import com.yuyakaido.android.gaia.community.databinding.ItemCommunityBinding
import com.yuyakaido.android.gaia.core.domain.entity.Community

class CommunityItem(
  val community: Community.Detail,
  private val listener: (Community.Detail) -> Unit
) : BindableItem<ItemCommunityBinding>() {

  override fun initializeViewBinding(view: View): ItemCommunityBinding {
    return ItemCommunityBinding.bind(view)
  }

  override fun getLayout(): Int {
    return R.layout.item_community
  }

  override fun bind(binding: ItemCommunityBinding, position: Int) {
    Glide
      .with(binding.root.context)
      .load(community.icon)
      .circleCrop()
      .into(binding.icon)
    binding.name.text = community.name.value
    binding.root.setOnClickListener { listener.invoke(community) }
  }

  override fun isSameAs(other: Item<*>): Boolean {
    return if (other is CommunityItem) {
      other.community.id == community.id
    } else {
      false
    }
  }

  override fun hasSameContentAs(other: Item<*>): Boolean {
    return if (other is CommunityItem) {
      other.community == community
    } else {
      false
    }
  }

}