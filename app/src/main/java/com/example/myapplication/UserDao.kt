package com.example.myapplication

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User): Long

    @Update
    fun updateUser(newUser: User)

    @Query("select * from User")
    fun loadAllUsers(): List<User>

    @Query("select * from User where idd = :idd")
    fun chazhao(idd: Long): List<User>

    @Delete
    fun deleteUser(user: User)

    @Query("delete from User where idd = :idd")
    fun deleteUserbyidd(idd: Long): Int
    @Query("select * from User where Year=:aa")
    fun nian(aa:String):List<User>
    @Query("select * from User where Year=:aa and Month=:bb")
    fun yue(aa:String,bb:String):List<User>
    @Query("select * from User where Year=:aa and Month=:bb and Day=:cc" )
    fun day(aa:String,bb:String,cc:String):List<User>
    @Query("select * from User where Categroy like'%'||:aa||'%' or Amount like'%'||:aa||'%' or times like'%'||:aa||'%'")
    fun chaxun(aa:String): List<User>

}