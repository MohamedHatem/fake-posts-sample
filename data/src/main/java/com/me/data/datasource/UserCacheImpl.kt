package com.me.data.datasource


import android.util.Log
import com.me.data.db.AppDatabase
import com.me.data.db.UserDao
import com.me.data.entities.mapToData
import com.me.data.entities.mapToDomain
import com.me.domain.entities.UserEntity
import com.me.domain.repositories.UserRepositoryImpl
import io.reactivex.Flowable
import java.lang.Exception

class UserCacheImpl(private val database: AppDatabase) : UserCacheDataSource {

    val LOG_TAG = "UserCacheImpl"

    private val dao: UserDao = database.getUsersDao()

    override fun getUsers(): Flowable<List<UserEntity>> {
        return dao.getAllUsers().map {
            if (it.isEmpty()) {
                throw Exception("Empty List")
            }
            it.mapToDomain()
        }
    }

    override fun setUsers(usersList: List<UserEntity>) {
        dao.clear()
        Log.d(LOG_TAG, "save remote into db $usersList")
        dao.saveAllUsers(usersList.mapToData())
    }

    override fun getUser(userId: String): Flowable<UserEntity> {
        return dao.getUser(userId).map {
            it.mapToDomain()
        }
    }

    override fun setUser(post: UserEntity) {
        dao.saveUser(post.mapToData())
    }


}