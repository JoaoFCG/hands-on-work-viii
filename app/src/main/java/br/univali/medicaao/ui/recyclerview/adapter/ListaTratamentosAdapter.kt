package br.univali.medicaao.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.univali.medicaao.R
import br.univali.medicaao.databinding.TratamentoItemBinding
import br.univali.medicaao.model.Tratamento

class ListaTratamentosAdapter(
    private val context: Context,
    tratamentos: List<Tratamento> = emptyList(),
    var quandoClicaNoItem: (tratamento: Tratamento) -> Unit = {}
) : RecyclerView.Adapter<ListaTratamentosAdapter.ViewHolder>() {
    private val tratamentos = tratamentos.toMutableList()

    inner class ViewHolder(private val binding: TratamentoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var tratamento: Tratamento

        init {
            itemView.setOnClickListener {
                if (::tratamento.isInitialized) {
                    quandoClicaNoItem(tratamento)
                }
            }
        }

        fun vincula(tratamento: Tratamento) {
            this.tratamento = tratamento
            val nome = binding.tratamentoItemNome
            nome.text = tratamento.nome
            val horario = binding.tratamentoItemHorario
            horario.text = tratamento.horario
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = TratamentoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tratamento = tratamentos[position]
        holder.vincula(tratamento)
    }

    override fun getItemCount(): Int = tratamentos.size

    fun atualiza(tratamentos: List<Tratamento>) {
        this.tratamentos.clear()
        this.tratamentos.addAll(tratamentos)
        notifyDataSetChanged()
    }
}
