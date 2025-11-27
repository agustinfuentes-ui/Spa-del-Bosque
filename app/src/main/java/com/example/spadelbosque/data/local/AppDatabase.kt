package com.example.spadelbosque.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos principal de la aplicación usando Room.
 * Define las tablas (entities) y la versión de la BD.
 */
@Database(
    entities = [UsuarioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spa_db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)   // <--- evita crash por cambios de schema
                    .build()
                    .also { INSTANCE = it }
            }
    }
}