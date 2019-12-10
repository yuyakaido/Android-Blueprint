package com.yuyakaido.android.gaia.article.list

import android.app.Application
import com.yuyakaido.android.gaia.core.infrastructure.RedditAuthService
import dagger.Module
import dagger.Provides

@Module
class ArticleListModule : ArticleListModuleType {

  @Provides
  override fun provideArticleListViewModel(
    application: Application,
    fragment: ArticleListFragment,
    service: RedditAuthService
  ): ArticleListViewModelType {
    return ArticleListViewModel(
      application = application,
      page = fragment.getArticleListPage(),
      service = service
    )
  }

}