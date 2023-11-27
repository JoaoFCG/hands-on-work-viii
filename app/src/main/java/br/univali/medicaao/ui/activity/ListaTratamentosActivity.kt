package br.univali.medicaao.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.univali.medicaao.database.AppDatabase
import br.univali.medicaao.databinding.ActivityListaTratamentosBinding
import br.univali.medicaao.ui.recyclerview.adapter.ListaTratamentosAdapter

class ListaTratamentosActivity : AppCompatActivity() {
    private val adapter = ListaTratamentosAdapter(context = this)
    private val binding by lazy {
        ActivityListaTratamentosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()
        val db =  AppDatabase.instancia(this)
        val tratamentoDao = db.tratamentoDao()
        adapter.atualiza(tratamentoDao.buscaTodos())
    }

    private fun configuraFab() {
        val fab = binding.activityListaTratamentosFab
        fab.setOnClickListener {
            vaiParaFormularioTratamento()
        }
    }

    private fun vaiParaFormularioTratamento() {
        val intent = Intent(this, FormularioTratamentoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaTratamentosRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.quandoClicaNoItem = {
            val intent = Intent(
                this,
                DetalhesTratamentoActivity::class.java
            ).apply {
                putExtra(CHAVE_TRATAMENTO_ID, it.uid)
            }
            startActivity(intent)
        }
    }
}