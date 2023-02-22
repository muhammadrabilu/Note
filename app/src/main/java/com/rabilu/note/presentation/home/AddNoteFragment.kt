package com.rabilu.note.presentation.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rabilu.note.R
import com.rabilu.note.databinding.FragmentAddNoteBinding
import com.rabilu.note.model.Note
import com.rabilu.note.util.Constance
import com.rabilu.note.util.getDateTime

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private lateinit var binding: FragmentAddNoteBinding
    private val db = Firebase.firestore;
    private val auth = Firebase.auth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNoteBinding.bind(view)

        val updatingNote = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable("note", Note::class.java)
        } else {
            requireArguments().getParcelable("note")
        }

        if (updatingNote != null) {
            binding.apply {
                noteBody.setText(updatingNote.note)
                noteTitle.setText(updatingNote.title)
                date.text = getDateTime(updatingNote.date!!)
                btnSaveNote.text = buildString {
                    append("Update")
                }
            }
        }


        binding.btnSaveNote.setOnClickListener {

            if (binding.noteTitle.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "what is the title of this note",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.noteBody.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "No note to save",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.progressBar5.visibility = View.VISIBLE
                val noteid = if (binding.btnSaveNote.text == "Update") {
                    updatingNote!!.id.toString()
                } else {
                    db.collection(Constance.NOTES.name).document().id
                }

                val note = Note(
                    noteid,
                    title = binding.noteTitle.text.toString(),
                    note = binding.noteBody.text.toString(),
                    date = System.currentTimeMillis(),
                    uid = auth.currentUser!!.uid
                )

                db.collection(Constance.NOTES.name).document(noteid)
                    .set(note).addOnSuccessListener {
                        binding.progressBar5.visibility = View.GONE
                        Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }.addOnFailureListener {
                        binding.progressBar5.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }


    }
}