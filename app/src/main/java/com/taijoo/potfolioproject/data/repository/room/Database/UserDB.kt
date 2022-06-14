package com.taijoo.potfolioproject.data.repository.room.Database

import android.content.Context
import android.util.Log
import androidx.room.*
import com.taijoo.potfolioproject.data.repository.room.Converters.UserConverters
import com.taijoo.potfolioproject.data.repository.room.Dao.CalendarDao
import com.taijoo.potfolioproject.data.repository.room.Dao.MemoDao
import com.taijoo.potfolioproject.data.repository.room.Dao.UserDao
import com.taijoo.potfolioproject.data.repository.room.entity.CalendarEntity
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.data.repository.room.entity.User
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration


@Database(entities = [User::class  , Memo::class  , CalendarEntity::class], version = 6 , exportSchema = false)
@TypeConverters(UserConverters::class)
abstract class UserDB : RoomDatabase(){


    abstract fun userDao() : UserDao
    abstract fun memoDao() : MemoDao
    abstract fun calendarDao() : CalendarDao

    companion object{
        private var INSTANCE : UserDB? = null


        private val MIGRATION: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS CalendarBackup (" +
                            "cal_seq INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "memo_seq INTEGER NOT NULL)"
                )

                database.execSQL("INSERT INTO CalendarBackup (cal_seq, memo_seq) SELECT cal_seq, memo_seq FROM Calendar")

                database.execSQL("ALTER TABLE 'CalendarBackup' ADD COLUMN 'year' INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE 'CalendarBackup' ADD COLUMN 'month' INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE 'CalendarBackup' ADD COLUMN 'day' INTEGER NOT NULL DEFAULT 0")

                database.execSQL("DROP TABLE Calendar")
                database.execSQL("ALTER TABLE CalendarBackup RENAME to Calendar")

            }
        }

        private val MIGRATION2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS CalendarBackup (" +
                            "cal_seq INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "memo_seq INTEGER NOT NULL)"
                )

                database.execSQL("INSERT INTO CalendarBackup (cal_seq, memo_seq) SELECT cal_seq, memo_seq FROM Calendar")

                database.execSQL("ALTER TABLE 'CalendarBackup' ADD COLUMN 'year' INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE 'CalendarBackup' ADD COLUMN 'month' INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE 'CalendarBackup' ADD COLUMN 'day' INTEGER NOT NULL DEFAULT 0")

                database.execSQL("DROP TABLE Calendar")
                database.execSQL("ALTER TABLE CalendarBackup RENAME to Calendar")

            }
        }

        private val MIGRATION3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS CalendarBackup (" +
                            "cal_seq INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "memo_seq INTEGER NOT NULL," +
                            "year INTEGER NOT NULL," +
                            "month INTEGER NOT NULL," +
                            "day INTEGER NOT NULL)"
                )

                database.execSQL("INSERT INTO CalendarBackup (cal_seq, memo_seq , year,month,day) SELECT cal_seq, memo_seq,year,month,day FROM Calendar")

                database.execSQL("ALTER TABLE 'CalendarBackup' ADD COLUMN 'date' TEXT NOT NULL DEFAULT ''")

                database.execSQL("DROP TABLE Calendar")
                database.execSQL("ALTER TABLE CalendarBackup RENAME to Calendar")

            }
        }

        fun getInstance(context: Context):UserDB?{

            if(INSTANCE == null){
                synchronized(UserDB::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDB::class.java, "User.db")
                        .addMigrations(MIGRATION,MIGRATION2_3,MIGRATION3_4)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}