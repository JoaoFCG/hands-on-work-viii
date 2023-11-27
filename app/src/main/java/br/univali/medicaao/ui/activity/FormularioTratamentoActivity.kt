package br.univali.medicaao.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.univali.medicaao.R
import br.univali.medicaao.database.AppDatabase
import br.univali.medicaao.database.dao.TratamentoDao
import br.univali.medicaao.databinding.ActivityFormularioTratamentoBinding
import br.univali.medicaao.model.Tratamento
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class FormularioTratamentoActivity :
    AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioTratamentoBinding.inflate(layoutInflater)
    }
    private var idTratamento = 0L
    private val tratamentoDao: TratamentoDao by lazy {
        val db = AppDatabase.instancia(this)
        db.tratamentoDao()
    }
    private var horaRef = 0
    private var minutoRef = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Novo Tratamento"
        tentaCarregarTratamento()
        val horario = configuraTimePicker()
        configuraBotaoConcluir(horario)
    }

    private fun tentaCarregarTratamento() {
        idTratamento = intent.getLongExtra(CHAVE_TRATAMENTO_ID, 0L)
    }

    override fun onResume() {
        super.onResume()
        tentaBuscarTratamento()
    }

    private fun tentaBuscarTratamento() {
        tratamentoDao.buscaPorId(idTratamento)?.let {
            title = "Editar Tratamento"
            preencheCampos(it)
        }
    }

    private fun preencheCampos(tratamento: Tratamento) {
        binding.activityFormularioNome
            .setText(tratamento.nome)
        binding.activityFormularioFrequencia
            .setText(tratamento.frequencia.toString())
        horaRef = tratamento.hora
        minutoRef = tratamento.minuto
        binding.activityFormularioPreviewHorario.setText(tratamento.horario)
        binding.activityFormularioDoses.setText(tratamento.doses.toString())
    }

    fun configuraBotaoConcluir(horario: ArrayList<Int>) {
        val botaoConcluir = binding.activityFormularioBotaoConcluir
        botaoConcluir.setOnClickListener {
            val novoTratamento = criaTratamento(horario)
            if (novoTratamento.frequencia > 24|| novoTratamento.frequencia < 1) {
                Toast.makeText(this, "ERRO - Frequência deve ser entre 1 e 24 horas", Toast.LENGTH_SHORT).show()
            } else if (novoTratamento.doses == 0) {
                tratamentoDao.buscaPorId(idTratamento)?.let {
                    tratamentoDao.exclui(it)
                }
                finish()
            } else {
                tratamentoDao.salva(novoTratamento)
                finish()
            }
        }
    }

    fun criaTratamento(horario: ArrayList<Int>): Tratamento {
        val campoNome = binding.activityFormularioNome
        val nome = campoNome.text.toString()
        val campoFrequencia = findViewById<EditText>(R.id.activity_formulario_frequencia)
        val frequenciaEmTexto = campoFrequencia.text.toString()
        val frequencia = if (frequenciaEmTexto.isBlank()) {
            0
        } else {
            frequenciaEmTexto.toInt()
        }
        val horaEmTexto = if(horario.size > 0 ) {
            horario.get(horario.size - 2).toString()
        } else {
            horaRef.toString()
        }
        val hora = if (horaEmTexto.isBlank()) {
            0
        } else {
            horaEmTexto.toInt()
        }
        val minutoEmTexto = if(horario.size > 0 ) {
            horario.get(horario.size - 1).toString()
        } else {
            minutoRef.toString()
        }
        val minuto = if (minutoEmTexto.isBlank()) {
            0
        } else {
            minutoEmTexto.toInt()
        }
        val horario = formataHorario(hora, minuto)
        val campoDoses = binding.activityFormularioDoses
        val dosesEmTexto = campoDoses.text.toString()
        val doses = if (dosesEmTexto.isBlank()) {
            0
        } else {
            dosesEmTexto.toInt()
        }

        return Tratamento(
            uid = idTratamento,
            nome = nome,
            frequencia = frequencia,
            hora = hora,
            minuto = minuto,
            horario = horario,
            doses = doses
        )
    }

    private fun configuraTimePicker(): ArrayList<Int> {
        val horario = ArrayList<Int>()
        val botaoHorario = findViewById<Button>(R.id.activity_formulario_horario)
        val previewHorario = findViewById<TextView>(R.id.activity_formulario_preview_horario)
        botaoHorario.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("Horário do alarme:")
                .setHour(horaRef)
                .setMinute(minutoRef)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            materialTimePicker.show(supportFragmentManager, "MainActivity")

            materialTimePicker.addOnPositiveButtonClickListener {
                horaRef = materialTimePicker.hour
                minutoRef = materialTimePicker.minute
                horario.add(horaRef)
                horario.add(minutoRef)
                previewHorario.text = formataHorario(materialTimePicker.hour, materialTimePicker.minute)
            }
        }
        return horario
    }

    private fun formataHorario(hora: Int, minuto: Int): String {
        val horarioFormatado = if (minuto.toString().length < 2) {
            "${hora}:0${minuto}"
        } else {
            "${hora}:${minuto}"
        }
        return horarioFormatado
    }
}