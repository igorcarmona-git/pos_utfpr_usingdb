package com.example.pos_utfpr_usingdb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.pos_utfpr_usingdb.R
import com.example.pos_utfpr_usingdb.entity.Cadastro

class ElementoImageListAdapter(val context: Context, val elements: MutableList<Cadastro>):
    BaseAdapter() {
    //retorna a quantidade de elementos da lista
    override fun getCount(): Int {
        return elements.size
    }

    //retorna o elemento da lista na posição especificada
    override fun getItem(pos: Int): Any {
        return elements[pos]
    }

    //retorna o ‘id’ do elemento da lista na posição especificada
    override fun getItemId(position: Int): Long {
        return elements[position].id.toLong()
    }

    //retorna a ‘view’ da lista na posição especificada
    override fun getView(
        pos: Int,
        componentOrigin: View?,
        rootComponent: ViewGroup?
    ): View {
        val inflater = LayoutInflater.from(context)
        val v = componentOrigin ?: inflater.inflate(R.layout.elemento_image_listview, rootComponent, false)

        val tvNome = v.findViewById<TextView>(R.id.tv_nomeElementoLista)
        val tvCell = v.findViewById<TextView>(R.id.tv_cellElementoLista)
        val ivAvatar = v.findViewById<ImageView>(R.id.iv_avatar)
        val btEditButton = v.findViewById<ImageButton>(R.id.bt_editElementoLista)

        val item = elements[pos]
        tvNome.text = item.nome
        tvCell.text = item.cellphone

        if(pos % 2 == 0) {
            ivAvatar.setImageResource(android.R.drawable.star_big_on)
        }else{
            ivAvatar.setImageResource(R.drawable.ic_avatar)
        }

        btEditButton.setOnClickListener {
            Toast.makeText(context, "Editando item na posição: $pos", Toast.LENGTH_SHORT).show()
        }

        return v
    }
}