package com.yuyakaido.android.gaia.article.list

import com.yuyakaido.android.gaia.core.AppState
import com.yuyakaido.android.gaia.core.ArticleAction
import com.yuyakaido.android.gaia.core.ArticleState
import com.yuyakaido.android.gaia.core.SuspendableArticleAction
import com.yuyakaido.android.gaia.core.domain.entity.Article
import com.yuyakaido.android.gaia.core.domain.entity.ArticleListSource
import com.yuyakaido.android.gaia.core.domain.repository.ArticleRepositoryType
import com.yuyakaido.android.gaia.core.domain.repository.VoteRepositoryType
import com.yuyakaido.android.reduxkit.DispatcherType
import com.yuyakaido.android.reduxkit.SelectorType
import javax.inject.Inject

class ArticleListActionCreator @Inject constructor(
  private val articleRepository: ArticleRepositoryType,
  private val voteRepository: VoteRepositoryType
) {

  fun refresh(source: ArticleListSource): ArticleAction {
    return ArticleAction.ToInitial(source)
  }

  fun paginate(source: ArticleListSource): SuspendableArticleAction {
    return object : SuspendableArticleAction {
      override suspend fun execute(
        selector: SelectorType<ArticleState>,
        dispatcher: DispatcherType<AppState>
      ) {
        val state = selector.select().find(source)
        if (state.canPaginate()) {
          dispatcher.dispatch(
            ArticleAction.ToLoading(
              source = source
            )
          )
          try {
            val item = source.articles(
              repository = articleRepository,
              after = state.after
            )
            dispatcher.dispatch(
              ArticleAction.ToIdeal(
                source = source,
                articles = item.entities,
                after = item.after
              )
            )
          } catch (e: Exception) {
            dispatcher.dispatch(
              ArticleAction.ToError(source)
            )
          }
        }
      }
    }
  }

  fun upvote(
    source: ArticleListSource,
    article: Article
  ): SuspendableArticleAction {
    return object : SuspendableArticleAction {
      override suspend fun execute(
        selector: SelectorType<ArticleState>,
        dispatcher: DispatcherType<AppState>
      ) {
        val votedArticle = voteRepository.upvote(article)
        dispatcher.dispatch(
          ArticleAction.Update(
            source = source,
            article = votedArticle
          )
        )
      }
    }
  }

  fun downvote(
    source: ArticleListSource,
    article: Article
  ): SuspendableArticleAction {
    return object : SuspendableArticleAction {
      override suspend fun execute(
        selector: SelectorType<ArticleState>,
        dispatcher: DispatcherType<AppState>
      ) {
        val votedArticle = voteRepository.downvote(article)
        dispatcher.dispatch(
          ArticleAction.Update(
            source = source,
            article = votedArticle
          )
        )
      }
    }
  }

}