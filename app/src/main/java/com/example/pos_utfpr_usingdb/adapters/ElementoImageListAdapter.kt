package com.example.pos_utfpr_usingdb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.pos_utfpr_usingdb.R
import com.example.pos_utfpr_usingdb.databinding.ElementoImageListviewBinding
import com.example.pos_utfpr_usingdb.entity.Cadastro

class ElementoImageListAdapter(
    context: Context,
    private val elements: MutableList<Cadastro>,
    private val onEditClick: (Cadastro, Int) -> Unit
) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return elements.size
    }

    override fun getItem(position: Int): Cadastro {
        return elements[position]
    }

    override fun getItemId(position: Int): Long {
        return elements[position].id.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val binding: ElementoImageListviewBinding
        val view: View

        if (convertView == null) {
            binding = ElementoImageListviewBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ElementoImageListviewBinding
        }

        val item = getItem(position)

        binding.tvNomeElementoLista.text = item.nome
        binding.tvCellElementoLista.text = item.cellphone

        val avatarResource = if (position % 2 == 0) {
            android.R.drawable.star_big_on
        } else {
            R.drawable.ic_avatar
        }

        binding.ivAvatar.setImageResource(avatarResource)

        binding.btEditElementoLista.setOnClickListener {
            onEditClick(item, position)
        }

        return view
    }
}