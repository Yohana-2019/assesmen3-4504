package org.d3if3118.hitungbmr.ui.lainnya

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3if3118.hitungbmr.network.ApiStatus
import org.d3if3118.hitungbmr.databinding.FragmentLainnyaBinding


class LainnyaFragment : Fragment() {
    private val viewModel: LainnyaViewModel by lazy {
        ViewModelProvider(this)[LainnyaViewModel::class.java]
    }
    private lateinit var binding: FragmentLainnyaBinding
    private lateinit var myAdapter: MainAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLainnyaBinding.inflate(layoutInflater, container, false)
        myAdapter = MainAdapter()
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = myAdapter
            setHasFixedSize(true)
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData().observe(viewLifecycleOwner, {
            myAdapter.updateData(it)
        })

        viewModel.getStatus().observe(viewLifecycleOwner, {
            updateProgress(it)
        })

        viewModel.scheduleUpdater(requireActivity().application)
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }
            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }
}