package com.rabilu.note.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rabilu.note.databinding.NoteListItemBinding
import com.rabilu.note.model.Note
import com.rabilu.note.util.getDateTime

class NoteAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<Note, NoteAdapter.MyViewHolder>(DifUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = NoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { onItemClickListener.onNoteClick(currentItem) }
    }


    inner class MyViewHolder(private val itemBinding: NoteListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(note: Note) {
            itemBinding.title.text = note.title
            itemBinding.dateTime.text = note.date?.let { getDateTime(it) }
        }
    }

}

class DifUtil : ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
}

interface OnItemClickListener {
    fun onNoteClick(note: Note)
}