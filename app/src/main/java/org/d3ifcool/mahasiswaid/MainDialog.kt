package org.d3ifcool.mahasiswaid

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import org.d3ifcool.mahasiswaid.data.Mahasiswa
import org.d3ifcool.mahasiswaid.data.MahasiswaDb

class MainDialog : DialogFragment() {
    private val viewModel: MainViewModel by navGraphViewModels(R.id.main) {
        MainViewModelFactory(MahasiswaDb.getInstance(requireContext()).dao)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_main, null, false)
        val builder = AlertDialog.Builder(requireContext())

        with(builder) {
            setTitle(R.string.mahasiswa_baru)
            setView(view)
            setPositiveButton(R.string.simpan) { _, _ ->
                val mahasiswa = getData(view) ?: return@setPositiveButton
                viewModel.insertData(mahasiswa)
            }
            setNegativeButton(R.string.batal) { _, _ -> dismiss() }
        }

        return builder.create()
    }

    private fun getData(view: View): Mahasiswa? {
        val nimEditText = view.findViewById<EditText>(R.id.nimEditText)
        val namaEditText = view.findViewById<EditText>(R.id.namaEditText)

        if (nimEditText.text.isEmpty()) {
            showMessage(R.string.nim_wajib_diisi)
            return null
        }

        if (nimEditText.text.length != 10) {
            showMessage(R.string.nim_harus_10chars)
            return null
        }

        if (namaEditText.text.isEmpty()) {
            showMessage(R.string.nama_wajib_diisi)
            return null
        }

        val args = MainDialogArgs.fromBundle(requireArguments())
        return Mahasiswa(
            nim = nimEditText.text.toString(),
            nama = namaEditText.text.toString(),
            kelas = args.kelas
        )
    }

    private fun showMessage(messageResId: Int) {
        val toast = Toast.makeText(requireContext(), messageResId, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}