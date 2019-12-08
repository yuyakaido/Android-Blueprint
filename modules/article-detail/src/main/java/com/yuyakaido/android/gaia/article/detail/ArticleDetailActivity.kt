package com.yuyakaido.android.gaia.article.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.yuyakaido.android.gaia.article.detail.databinding.ActivityArticleDetailBinding
import com.yuyakaido.android.gaia.core.Article
import com.yuyakaido.android.gaia.core.BaseActivity
import com.yuyakaido.android.gaia.core.ViewModelFactory
import javax.inject.Inject

class ArticleDetailActivity : BaseActivity() {

  companion object {
    private val ARTICLE = Article::class.java.simpleName

    fun createIntent(context: Context, article: Article): Intent {
      return Intent(context, ArticleDetailActivity::class.java)
        .apply { putExtra(ARTICLE, article) }
    }
  }

  @Inject
  internal lateinit var factory: ViewModelFactory<ArticleDetailViewModel>

  private val viewModel: ArticleDetailViewModel by viewModels { factory }
  private val binding by lazy { ActivityArticleDetailBinding.inflate(layoutInflater) }

  internal fun getArticle(): Article {
    return intent.getParcelableExtra(ARTICLE) as Article
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    setupDetail()
    viewModel.onBind()
  }

  private fun setupDetail() {
    viewModel.title
      .observe(this) { title ->
        binding.title.text = title
      }
    viewModel.thumbnail
      .observe(this) { thumbnail ->
        Glide.with(binding.root.context)
          .load(thumbnail)
          .placeholder(ColorDrawable(Color.LTGRAY))
          .into(binding.thumbnail)
      }
    viewModel.comments
      .observe(this) { comments ->
        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.comments.layoutManager = LinearLayoutManager(this)
        binding.comments.adapter = adapter
        adapter.updateAsync(comments.map { comment -> CommentItem(comment = comment) })
      }
  }

}