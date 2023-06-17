package org.d3if3118.hitungbmr.ui.histori

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.d3if3118.hitungbmr.R
import org.d3if3118.hitungbmr.data.SettingDataStore
import org.d3if3118.hitungbmr.data.dataStore
import org.d3if3118.hitungbmr.databinding.FragmentHistoriBinding
import org.d3if3118.hitungbmr.db.BmrDb

class HistoriFragment : Fragment() {

    private val viewModel: HistoriViewModel by lazy {
        val db = BmrDb.getInstance(requireContext())
        val factory = HistoriViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HistoriViewModel::class.java]
    }

    private lateinit var binding: FragmentHistoriBinding
    private lateinit var myAdapter: HistoriAdapter
    private var isLinearLayoutManager = true
    private lateinit var layoutDataStore: SettingDataStore

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        } else {
            binding.recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        }
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null) return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(requireContext(),
                    R.drawable.ic_baseline_grid_view_24)
            else ContextCompat.getDrawable(requireContext(),
                R.drawable.ic_baseline_view_list_24)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoriBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myAdapter = HistoriAdapter()
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = myAdapter
            setHasFixedSize(true)
        }

        layoutDataStore = SettingDataStore(requireContext().dataStore)
        layoutDataStore.preferenceFlow.asLiveData()
            .observe(viewLifecycleOwner, { value ->
                isLinearLayoutManager = value
                chooseLayout()
                activity?.invalidateOptionsMenu()
            })

        viewModel.data.observe(viewLifecycleOwner, {
            binding.emptyView.visibility = if (it.isEmpty())
                View.VISIBLE else View.GONE
            myAdapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.histori_menu, menu)
        val layoutButton = menu?.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager

                lifecycleScope.launch {
                    layoutDataStore.saveLayoutToPreferencesStore(
                        isLinearLayoutManager, requireContext()
                    )
                }
                chooseLayout()
                setIcon(item)
                return true
            }
            R.id.menu_hapus -> {
                hapusData()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun hapusData() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.konfirmasi_hapus)
            .setPositiveButton(getString(R.string.hapus)) { _, _ ->
                viewModel.hapusData()
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}