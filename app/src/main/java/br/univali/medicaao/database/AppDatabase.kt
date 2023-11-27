package br.univali.medicaao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.univali.medicaao.database.dao.TratamentoDao
import br.univali.medicaao.model.Tratamento

@Database(entities = [Tratamento::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tratamentoDao(): TratamentoDao

    companion object {
        fun instancia(context: Context) : AppDatabase {
           return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "medica_acao.db"
            ).allowMainThreadQueries()
                .build()
        }
    }
}