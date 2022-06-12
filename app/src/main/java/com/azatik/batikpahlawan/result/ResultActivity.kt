package com.azatik.batikpahlawan.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azatik.batikpahlawan.databinding.ActivityMainBinding
import com.azatik.batikpahlawan.databinding.ActivityResultBinding
import com.azatik.batikpahlawan.detail.DetailActivity
import com.azatik.batikpahlawan.main.MainAdapter
import com.azatik.batikpahlawan.main.ModelMain
import com.azatik.batikpahlawan.main.ViewModelBatik
import com.azatik.batikpahlawan.retrofit.MotifBatik
import com.azatik.batikpahlawan.retrofit.batikResponse
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private  val viewModel: ViewModelBatik by viewModels()
    private val modelMain= mutableMapOf<String,ModelMain>()
    private lateinit var adapter: ResultAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val res = intent.getParcelableExtra<batikResponse>("LIST_RESULT")!!
        adapter = ResultAdapter()
        adapter.notifyDataSetChanged()
        getListBatik()
        binding.apply {
            rvListBatik.layoutManager = LinearLayoutManager(this@ResultActivity)
            rvListBatik.setHasFixedSize(true)
            rvListBatik.adapter = adapter
            adapter.setdata(res.items)
            binding.rvListBatik.visibility = View.VISIBLE
            Log.e("TES",modelMain.toString())
            adapter.setOnItemClickCallback(object :
                ResultAdapter.OnItemClickCallBack {

                override fun onItemClicked(data: MotifBatik) {
                    val intent = Intent(this@ResultActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.BATIK_LIST, modelMain[data.Motif_Batik])
                    Log.e("TES_ER",modelMain[data.Motif_Batik].toString())
                    this@ResultActivity.startActivity(intent)
                }
            })
//            viewModel.getResult().observe(this@ResultActivity) {
//                if (it != null) {
//                    adapter.setdata(it as ArrayList<MotifBatik>)
//                    binding.rvListBatik.visibility = View.VISIBLE
//                }
//
//            }
        }


    }
    private fun getListBatik() {
        try {
            val stream = assets.open("batik_list.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val strContent = String(buffer, StandardCharsets.UTF_8)
            try {
                val jsonObject = JSONObject(strContent)
                val jsonArray = jsonObject.getJSONArray("daftar_batik")
                for (i in 0 until jsonArray.length()) {
                    val jsonObjectData = jsonArray.getJSONObject(i)
                    val dataApi = ModelMain()
                    dataApi.nama = jsonObjectData.getString("nama")
                    dataApi.namaLengkap = jsonObjectData.getString("nama2")
                    dataApi.kategori = jsonObjectData.getString("kategori")
                    dataApi.image = jsonObjectData.getString("img")
                    dataApi.asal = jsonObjectData.getString("asal")
                    dataApi.harga = jsonObjectData.getString("harga")
                    dataApi.tutorial = jsonObjectData.getString("tutorial")
                    dataApi.details = jsonObjectData.getString("details")
                    dataApi.description = jsonObjectData.getString("description")
                    dataApi.capstone = jsonObjectData.getString("capstone")
                    modelMain[dataApi.nama] = dataApi
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } catch (ignored: IOException) {
            Toast.makeText(
                this@ResultActivity,
                "Oops, ada yang tidak beres. Coba ulangi beberapa saat lagi.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}