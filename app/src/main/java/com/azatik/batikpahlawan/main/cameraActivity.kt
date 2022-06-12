package com.azatik.batikpahlawan.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.azatik.batikpahlawan.databinding.ActivityCameraBinding
import com.azatik.batikpahlawan.detail.DetailActivity
import com.azatik.batikpahlawan.main.MainActivity.Companion.CAMERA_X

class cameraActivity : AppCompatActivity() {

    private var captureImg: ImageCapture? = null
    private var cameraRotate: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shootCam.setOnClickListener { takePhoto() }
        binding.rotateCam.setOnClickListener {
            cameraRotate =
                if (cameraRotate == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    private fun startCamera() {
        val Providercamera = ProcessCameraProvider.getInstance(this)
        Providercamera.addListener({
            val Provider: ProcessCameraProvider = Providercamera.get()
            val showPv = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.pvSatu.surfaceProvider) }

            captureImg = ImageCapture.Builder().build()

            try {
                Provider.unbindAll()
                Provider.bindToLifecycle(this, cameraRotate, showPv, captureImg)
            } catch (exc: Exception) {
                Helper.showToast(this, "camera x Failed.")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val capture = captureImg ?: return

        val img = Helper.saveImg(application)

        val resultOptions = ImageCapture.OutputFileOptions.Builder(img).build()
        capture.takePicture(resultOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Helper.showToast(this@cameraActivity, "Failed")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", img)
                    intent.putExtra("isBackCamera",
                        cameraRotate == CameraSelector.DEFAULT_BACK_CAMERA)
                    setResult(CAMERA_X, intent)
                    finish()
                }
            }
        )
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }


    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

}