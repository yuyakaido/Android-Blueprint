package com.yuyakaido.android.gaia.article.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yuyakaido.android.gaia.article.R
import com.yuyakaido.android.gaia.article.databinding.FragmentArticleDetailBinding
import com.yuyakaido.android.gaia.core.presentation.AppNavigatorType
import com.yuyakaido.android.gaia.core.presentation.BaseFragmentWithoutHilt
import javax.inject.Inject

class ArticleDetailFragment : BaseFragmentWithoutHilt<ArticleDetailViewModel>() {

  @Inject
  internal lateinit var appNavigator: AppNavigatorType

  override val viewModel: ArticleDetailViewModel by viewModels { factory }
  internal val args: ArticleDetailFragmentArgs by navArgs()
  private lateinit var binding: FragmentArticleDetailBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentArticleDetailBinding.inflate(inflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupDetail()
    setupComment()
  }

  private fun setupDetail() {
    viewModel
      .state
      .observe(viewLifecycleOwner) { state ->
        val article = state.article
        val b = binding.article
        b.community.text = getString(R.string.article_list_community, article.community.name)
        b.author.text = getString(R.string.article_list_author, article.author)
        b.title.text = article.title
        b.voteCount.text = article.voteCount.toString()
        b.upvote.setOnClickListener { viewModel.onUpvote(article) }
        b.downvote.setOnClickListener { viewModel.onDownvote(article) }
        when (article.likes) {
          null -> {
            b.upvote.setImageResource(R.drawable.ic_upvote_inactive)
            b.downvote.setImageResource(R.drawable.ic_downvote_inactive)
          }
          true -> {
            b.upvote.setImageResource(R.drawable.ic_upvote_active)
            b.downvote.setImageResource(R.drawable.ic_downvote_inactive)
            b.voteCount.setTextColor(
              ContextCompat.getColor(requireContext(), R.color.upvpte)
            )
          }
          false -> {
            b.upvote.setImageResource(R.drawable.ic_upvote_inactive)
            b.downvote.setImageResource(R.drawable.ic_downvote_active)
            b.voteCount.setTextColor(
              ContextCompat.getColor(requireContext(), R.color.downvote)
            )
          }
        }
        b.commentCount.text = article.comments.toString()
        if (article.thumbnail == Uri.EMPTY) {
          b.thumbnail.setImageDrawable(ColorDrawable(Color.LTGRAY))
        } else {
          Glide
            .with(binding.root.context)
            .load(article.thumbnail)
            .placeholder(ColorDrawable(Color.LTGRAY))
            .into(b.thumbnail)
        }
      }
  }

  private fun setupComment() {
    childFragmentManager
      .beginTransaction()
      .replace(
        R.id.fragment_container,
        appNavigator.newCommentListFragment(article = args.article)
      )
      .commitNow()
  }

}