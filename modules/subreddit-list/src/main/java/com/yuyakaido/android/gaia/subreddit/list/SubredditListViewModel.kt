package com.yuyakaido.android.gaia.subreddit.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yuyakaido.android.gaia.core.ListingDataResponse
import com.yuyakaido.android.gaia.core.RedditAuthService
import com.yuyakaido.android.gaia.core.Subreddit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SubredditListViewModel(
  application: Application,
  private val service: RedditAuthService
) : AndroidViewModel(
  application
) {

  val subreddits = MutableLiveData<List<Subreddit>>()

  fun onBind(page: SubredditListPage) {
    Timber.d("service = ${service.hashCode()}")
    if (subreddits.value == null) {
      service
        .subreddits(path = page.path)
        .enqueue(object : Callback<ListingDataResponse> {
          override fun onResponse(call: Call<ListingDataResponse>, response: Response<ListingDataResponse>) {
            response.body()?.let { body ->
              subreddits.postValue(body.toArticles())
            }
          }
          override fun onFailure(call: Call<ListingDataResponse>, t: Throwable) {
            Timber.e(t.toString())
          }
        })
    }
  }

  fun onUpvote(subreddit: Subreddit) {
    val pair: Pair<Int, Boolean?> = when {
      subreddit.likes == null -> 1 to true
      subreddit.likes == true -> 0 to null
      subreddit.likes == false -> 1 to true
      else -> 0 to null
    }
    service
      .vote(id = subreddit.name, dir = pair.first)
      .enqueue(object : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
          val newSubreddits = subreddits
            .value
            ?.map {
              if (it.id == subreddit.id) {
                subreddit.copy(likes = pair.second)
              } else {
                it
              }
            }
          subreddits.postValue(newSubreddits)
        }
        override fun onFailure(call: Call<Unit>, t: Throwable) {
          Timber.e(t.toString())
        }
      })
  }

  fun onDownvote(subreddit: Subreddit) {
    val pair: Pair<Int, Boolean?> = when {
      subreddit.likes == null -> -1 to false
      subreddit.likes == true -> -1 to false
      subreddit.likes == false -> 0 to null
      else -> 0 to null
    }
    service.vote(id = subreddit.name, dir = pair.first)
      .enqueue(object : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
          val newSubreddits = subreddits
            .value
            ?.map {
              if (it.id == subreddit.id) {
                subreddit.copy(likes = pair.second)
              } else {
                it
              }
            }
          subreddits.postValue(newSubreddits)
        }
        override fun onFailure(call: Call<Unit>, t: Throwable) {
          Timber.e(t.toString())
        }
      })
  }

}