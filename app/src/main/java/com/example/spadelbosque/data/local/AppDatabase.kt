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

    // DAO para acceder a la tabla de usuarios
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        // Singleton para evitar múltiples instancias de la BD
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos.
         * Si no existe, la crea.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spa_database" // Nombre del archivo de base de datos
                )
                    .fallbackToDestructiveMigration() // En desarrollo: borra BD si cambia versión
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}