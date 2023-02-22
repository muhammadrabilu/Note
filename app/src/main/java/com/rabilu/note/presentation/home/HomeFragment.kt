package com.rabilu.note.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rabilu.note.R
import com.rabilu.note.adapter.NoteAdapter
import com.rabilu.note.adapter.OnItemClickListener
import com.rabilu.note.databinding.FragmentHomeBinding
import com.rabilu.note.model.Note
import com.rabilu.note.model.User
import com.rabilu.note.util.Constance


class HomeFragment : Fragment(R.layout.fragment_home), OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        val recyclerVie = binding.recyclerView

        val adapter = NoteAdapter(this)

        recyclerVie.adapter = adapter
        recyclerVie.layoutManager = LinearLayoutManager(requireContext())

        Firebase.firestore.collection(Constance.USERS.name)
            .document(Firebase.auth.currentUser!!.uid).get().addOnCompleteListener {
                val data = it.result.toObject(User::class.java)

                binding.apply {
                    userName.text = "Hello ${data!!.fullName}"
                }

                binding.btnSignOut.setOnClickListener {
                    Firebase.auth.signOut()
                    findNavController().popBackStack(R.id.homeFragment, true)
                    findNavController().navigate(R.id.loginFragment)
                }
            }

        Firebase.firestore.collection(Constance.NOTES.name)
            .whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get().addOnCompleteListener {
                val data = it.result.toObjects(Note::class.java)
                adapter.submitList(data)
                binding.emptyNoteMessage.isVisible = data.size == 0
                binding.progressBar4.visibility = View.GONE
            }.addOnFailureListener {
                binding.progressBar4.visibility = View.GONE
                binding.errorMessage.text = it.localizedMessage
                binding.errorMessage.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                binding.progressBar4.visibility = View.VISIBLE
                val property =
                    adapter.currentList[viewHolder.adapterPosition]
                Firebase.firestore.collection(Constance.NOTES.name)
                    .document(property.id!!)
                    .delete().addOnCompleteListener {
                        binding.progressBar4.visibility = View.GONE
                        Snackbar.make(requireView(), "Note deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                Firebase.firestore.collection(Constance.NOTES.name)
                                    .document(property.id).set(property)
                            }.show()
                        adapter.notifyDataSetChanged()
                        Firebase.firestore.collection(Constance.NOTES.name)
                            .whereEqualTo("uid", Firebase.auth.currentUser!!.uid).get()
                            .addOnCompleteListener {
                                val data = it.result.toObjects(Note::class.java)
                                adapter.submitList(data)
                                binding.emptyNoteMessage.isVisible = data.size == 0
                                binding.progressBar4.visibility = View.GONE
                            }
                    }.addOnFailureListener {
                        binding.progressBar4.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }).attachToRecyclerView(recyclerVie)

    }

    override fun onNoteClick(note: Note) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAddNoteFragment(
                note
            )
        )
    }


}