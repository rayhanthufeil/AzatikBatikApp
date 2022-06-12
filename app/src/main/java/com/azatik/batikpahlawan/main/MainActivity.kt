package com.azatik.batikpahlawan.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azatik.batikpahlawan.R
import com.azatik.batikpahlawan.databinding.ActivityMainBinding
import com.azatik.batikpahlawan.detail.DetailActivity
import com.azatik.batikpahlawan.result.ResultActivity
import com.azatik.batikpahlawan.retrofit.batikResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private var getImg: File? = null
    private var imgBase64: String? = null
    private var storiesBitmap: Bitmap? = null
    private var modelMain: MutableList<ModelMain> = ArrayList()
    lateinit var mainAdapter: MainAdapter
    private lateinit var binding: ActivityMainBinding
    private val vm: ViewModelBatik by viewModels()




    companion object {
        const val CAMERA_X = 200

        const val DATA_USER = "USER"

        private val PERMISSIONS_MANIFEST = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        vm = ViewModelProvider(this).get(ViewModelBatik::class.java)
        permissionReq()

        binding.btnCamera.setOnClickListener { startCameraX() }

        //transparent background searchview
        val searchPlateId = searchData.getContext()
            .resources.getIdentifier("android:id/search_plate", null, null)

        val searchPlate = searchData.findViewById<View>(searchPlateId)
        searchPlate?.setBackgroundColor(Color.TRANSPARENT)
        searchData.setImeOptions(EditorInfo.IME_ACTION_DONE)
        searchData.setImeOptions(EditorInfo.IME_ACTION_DONE)
        searchData.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainAdapter.filter.filter(newText)
                return true
            }
        })

        rvListBatik.setLayoutManager(LinearLayoutManager(this))
        rvListBatik.setHasFixedSize(true)

        fabBackTop.setOnClickListener { view: View? ->
            rvListBatik.smoothScrollToPosition(
                0
            )
        }

        //get data json
        getListBatik()

    }


    private fun permissionReq() {
        if (!granted()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_MANIFEST, REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        permissionCode: Int,
        permissions: Array<String>,
        permissionResults: IntArray,
    ) {
        super.onRequestPermissionsResult(permissionCode, permissions, permissionResults)
        if (permissionCode == REQUEST_CODE) {
            if (!granted()) {
                Toast.makeText(this, getString(R.string.invalid_permission), Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun granted() = PERMISSIONS_MANIFEST.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        launchCameraX.launch(Intent(this, cameraActivity::class.java))
    }

    private val launchCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X) {
            val myFile = it.data?.getSerializableExtra("picture") as File

            getImg = myFile
            storiesBitmap = Helper.rotateBitmap(BitmapFactory.decodeFile(getImg?.path))

        }
//        uploadBatik()
        if(getImg!=null) {
            val file = Helper.reduceFileImage(getImg as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart =
                MultipartBody.Part.createFormData("file", file.name, requestImageFile)


            // upload image
            runBlocking {
                val res=vm.upload(imageMultipart)
                val intent=Intent(this@MainActivity, ResultActivity::class.java)
                intent.putExtra("LIST_RESULT",res)
                Log.e("TES",res.toString())
                startActivity(intent)
            }

        }
//        vm.isDone.observe(this){isDone->
//            if(isDone){
//                val intent=Intent(this@MainActivity, ResultActivity::class.java)
//                val bR= batikResponse(vm.listUser.value as ArrayList)
//                intent.putExtra("LIST_RESULT",bR)
//                startActivity(intent)
//            }
//
//        }


    }
//
//    private fun uploadBatik() {
//        if(getImg!=null) {
//            val file = Helper.reduceFileImage(getImg as File)
//            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val imageMultipart =
//                MultipartBody.Part.createFormData("file", file.name, requestImageFile)
//
//
//            // upload image
//            runBlocking {
//                vm.upload(imageMultipart)
//            }
//
//        }
//    }
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
                    modelMain.add(dataApi)
                }
                mainAdapter = MainAdapter(this, modelMain)
                rvListBatik.adapter = mainAdapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } catch (ignored: IOException) {
            Toast.makeText(
                this@MainActivity,
                "Oops, ada yang tidak beres. Coba ulangi beberapa saat lagi.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}