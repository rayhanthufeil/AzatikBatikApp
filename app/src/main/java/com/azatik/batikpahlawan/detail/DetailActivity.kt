package com.azatik.batikpahlawan.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azatik.batikpahlawan.R
import com.azatik.batikpahlawan.main.ModelMain
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.IOException

class DetailActivity : AppCompatActivity() {
    lateinit var nama: String
    lateinit var nama_lengkap: String
    lateinit var kategori: String
    lateinit var asal: String
    lateinit var tutorial: String
    lateinit var harga: String
    lateinit var details: String
    lateinit var description: String
    lateinit var capstone: String
    lateinit var modelMain: ModelMain

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //set transparent statusbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //get data intent
        modelMain = intent.getSerializableExtra(BATIK_LIST) as ModelMain
        if (modelMain != null) {
            nama = modelMain.nama
            nama_lengkap = modelMain.namaLengkap
            kategori = modelMain.kategori
            asal = modelMain.asal
            tutorial = modelMain.tutorial
            harga = modelMain.harga
            details = modelMain.details
            description = modelMain.description
            capstone = modelMain.capstone

            Glide.with(this)
                .load(modelMain.image)
                .into(imageBatik)

            tvNamaBatik.setText(nama)
            tvNamaLengkap.setText(nama_lengkap)
            tvKategori.setText(kategori)
            tvAsalBatik.setText(asal)
            tvHargaBatik.setText(harga)
            tvTutorial.setText("Batik Video Tutorial : $tutorial")
            tvDetails.setText("Details : $details")
            tvDesdription.setText("Description : $description")
            tvCapstone.setText("Capstone Project : $capstone")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val BATIK_LIST = "BATIK_LIST"
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }

    var url:String="https://www.mboxdrive.com/batik-of-java-a-visual-journey.mp3"
    var mediaPlayer= MediaPlayer()

    fun playinfo(view: View){
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        if(!mediaPlayer!!.isPlaying){

            Toast.makeText(this, "Info Batik is now Playing", Toast.LENGTH_SHORT).show()
            try {
                mediaPlayer!!.setDataSource(url)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
        else{
            Toast.makeText(this, "Info Batik is now Stopped", Toast.LENGTH_SHORT).show()
            try {
                mediaPlayer.pause()
                mediaPlayer.stop()
                mediaPlayer.reset()
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

}