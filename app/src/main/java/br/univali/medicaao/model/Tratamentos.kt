package br.univali.medicaao.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tratamento (
    @PrimaryKey(autoGenerate = true) val uid: Long = 0L,
    val nome: String?,
    val frequencia: Int,
    val hora: Int,
    val minuto: Int,
    val horario:String?,
    val doses:Int
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(uid)
        parcel.writeString(nome)
        parcel.writeInt(frequencia)
        parcel.writeInt(hora)
        parcel.writeInt(minuto)
        parcel.writeString(horario)
        parcel.writeInt(doses)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tratamento> {
        override fun createFromParcel(parcel: Parcel): Tratamento {
            return Tratamento(parcel)
        }

        override fun newArray(size: Int): Array<Tratamento?> {
            return arrayOfNulls(size)
        }
    }
}