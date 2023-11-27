package br.univali.medicaao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.univali.medicaao.R
import br.univali.medicaao.database.AppDatabase
import br.univali.medicaao.databinding.ActivityDetalhesTratamentoBinding
import br.univali.medicaao.model.Tratamento

class DetalhesTratamentoActivity : AppCompatActivity() {
    private var idTratamento: Long = 0L
    private var tratamento: Tratamento? = null
    private val binding by lazy {
        ActivityDetalhesTratamentoBinding.inflate(layoutInflater)
    }
    private val tratamentoDao by lazy {
        AppDatabase.instancia(this).tratamentoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Detalhes"
        tentaCarregarTratamento()
    }

    override fun onResume() {
        super.onResume()
        buscaTratamento()
    }

    private fun buscaTratamento() {
        tratamento = tratamentoDao.buscaPorId(idTratamento)
        tratamento?.let {
            preencheCampos(it)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_tratamento, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detalhes_tratamento_excluir -> {
                tratamento?.let { tratamentoDao.exclui(it) }
                finish()
            }

            R.id.menu_detalhes_tratamento_editar -> {
                Intent(this, FormularioTratamentoActivity::class.java).apply {
                    putExtra(CHAVE_TRATAMENTO_ID, idTratamento)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tentaCarregarTratamento() {
        idTratamento = intent.getLongExtra(CHAVE_TRATAMENTO_ID, 0L)
    }

    private fun preencheCampos(tratamentoCarregado: Tratamento) {
        with(binding) {
            activityDetalhesTratamentoNome.text = tratamentoCarregado.nome
            activityDetalhesTratamentoCategoriaFrequencia.text = "Frequência:"
            activityDetalhesTratamentoFrequencia.text =
                "A cada ${tratamentoCarregado.frequencia.toString()} horas"
            activityDetalhesTratamentoCategoriaHorario.text = "Próximo Horário:"
            activityDetalhesTratamentoHorario.text = tratamentoCarregado.horario.toString()
            activityDetalhesTratamentoCategoriaDoses.text = "Doses Restantes:"
            activityDetalhesTratamentoDoses.text = tratamentoCarregado.doses.toString()
        }
    }
}