package br.univali.medicaao.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.univali.medicaao.model.Tratamento

@Dao
interface TratamentoDao {
    @Query("SELECT * FROM Tratamento")
    fun buscaTodos() : List<Tratamento>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg tratamento: Tratamento)

    @Delete
    fun exclui(tratamento: Tratamento?)

    @Query("SELECT * FROM tratamento WHERE uid = :id")
    fun buscaPorId(id: Long) : Tratamento?
}