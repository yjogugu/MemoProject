package com.taijoo.potfolioproject.data.repository.room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.taijoo.potfolioproject.data.repository.room.entity.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun getUserAll(): Flow<User>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAll(user : User)

    @Query("UPDATE User SET user_seq = :user_seq , user_name = :user_name ,profile = :profile" +
            " , user_register_name = :user_register_name , email = :email , network_state = :networkState")

    fun updateUser(user_seq : Int , user_name: String , profile: String , user_register_name : String , email : String , networkState: Boolean)



    @Query("UPDATE User SET profile = :profile")
    fun updateUserProfile(profile : String)

    @Query("UPDATE User SET user_name = :user_name")
    fun updateUserName(user_name : String)

    @Query("UPDATE User SET network_state = :networkState")
    fun updateUserNetworkState(networkState : Boolean)
}