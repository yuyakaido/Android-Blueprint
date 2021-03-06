package com.yuyakaido.android.gaia

import com.yuyakaido.android.gaia.core.domain.entity.Session
import com.yuyakaido.android.gaia.core.domain.repository.SessionRepositoryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionRepository(
  database: SessionDatabase
) : SessionRepositoryType {

  private val dao = database.sessionDao()

  override suspend fun get(): List<Session.SignedIn> {
    return withContext(Dispatchers.IO) {
      dao.findAll().map { it.toEntity() }
    }
  }

  override suspend fun get(id: String): Session.SignedIn {
    return withContext(Dispatchers.IO) {
      dao.findBy(id).toEntity()
    }
  }

  override suspend fun post(session: Session.SignedIn) {
    return withContext(Dispatchers.IO) {
      dao.insert(SessionSchema.fromEntity(session))
    }
  }

  override suspend fun put(
    oldSession: Session.SignedIn,
    newSession: Session.SignedIn
  ) {
    return withContext(Dispatchers.IO) {
      dao.insert(
        SessionSchema.fromEntity(
          oldSession = oldSession,
          newSession = newSession
        )
      )
    }
  }

}