package org.d3if3118.hitungbmr.ui.hitung

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.d3if3118.hitungbmr.R
import org.d3if3118.hitungbmr.databinding.FragmentHitungBinding
import org.d3if3118.hitungbmr.db.BmrDb
import org.d3if3118.hitungbmr.model.HasilBmr

class HitungFragment : Fragment() {
    private lateinit var binding: FragmentHitungBinding

    private val viewModel: HitungViewModel by lazy {
        val db = BmrDb.getInstance(requireContext())
        val factory = HitungViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HitungViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHitungBinding.inflate(layoutInflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSubmit.setOnClickListener {
            hitungBmr()
        }

        viewModel.getHasilBmr().observe(requireActivity(), {
            showResult(it)
        })

        binding.btnReset.setOnClickListener {
            reset()
        }

        binding.shareButton.setOnClickListener { shareData() }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.activities_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinner.adapter = adapter
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_histori -> {
                findNavController().navigate(R.id.action_hitungFragment_to_historiFragment)
                return true
            }
            R.id.menu_about -> {
                findNavController().navigate(R.id.action_hitungFragment_to_aboutFragment)
                return true
            }
            R.id.menu_lainnya -> {
                findNavController().navigate(R.id.action_hitungFragment_to_lainnyaFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hitungBmr() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(context, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val usia = binding.usiaInp.text.toString()
        if (TextUtils.isEmpty(usia)) {
            Toast.makeText(context, R.string.usia_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val berat = binding.beratBadanInp.text.toString()
        if (TextUtils.isEmpty(berat)) {
            Toast.makeText(context, R.string.berat_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val tinggi = binding.tinggiBadanInp.text.toString()
        if (TextUtils.isEmpty(tinggi)) {
            Toast.makeText(context, R.string.tinggi_invalid, Toast.LENGTH_LONG).show()
            return
        }

        if (binding.spinner.selectedItem == "Pilih tipe aktivitas Anda!") {
            Toast.makeText(context, R.string.aktivitas_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val aktivitas = getBmrAktivitas()

        viewModel.hitungBmr(
            selectedId == R.id.priaRadioButton,
            usia.toDouble(),
            berat.toDouble(),
            tinggi.toDouble(),
            aktivitas
        )
    }

    private fun getBmrAktivitas(): String {
        val spinner = binding.spinner
        val bmrAktivitas = when (spinner.selectedItem) {
            "Hampir tidak pernah berolahraga" -> "Hampir tidak pernah berolahraga"
            "Jarang berolahraga" -> "Jarang olahrga"
            else -> "Sering olahraga atau beraktivitas fisik berat"
        }
        return bmrAktivitas
    }

    private fun showResult (result: HasilBmr?) {
        if (result == null) return

        binding.bmrTextView.text = getString(R.string.bmr_result, result.bmr)
        binding.kaloriTextView.text = getString(R.string.kalori_aktivitas_result, result.bmrAktivitas)
        binding.beratIdealTextView.text = getString(R.string.berat_ideal_result, result.beratIdeal)
        binding.shareButton.visibility = View.VISIBLE
    }

    private fun shareData() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        val gender = if (selectedId == R.id.priaRadioButton)
            getString(R.string.pria)
        else
            getString(R.string.wanita)
        val message = getString(
            R.string.bagikan_template,
            binding.beratBadanInp.text,
            binding.tinggiBadanInp.text,
            gender,
            binding.bmrTextView.text,
            binding.kaloriTextView.text,
            binding.beratIdealTextView.text
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager) != null) {
                    startActivity(shareIntent)
        }
    }

    private fun reset() {
        binding.radioGroup.clearCheck()
        binding.usiaInp.setText("")
        binding.beratBadanInp.setText("")
        binding.tinggiBadanInp.setText("")
        binding.spinner.setSelection(0)
        binding.bmrTextView.setText("")
        binding.kaloriTextView.setText("")
        binding.beratIdealTextView.setText("")
    }
}