package com.dicoding.nutriseeapp.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.dicoding.nutriseeapp.R
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraFragment : Fragment() {

    private lateinit var btnTakePhoto: Button
    private lateinit var btnChooseFromGallery: Button
    private lateinit var imgPreview: ImageView

    private var currentPhotoPath: String? = null

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentPhotoPath?.let {
                val photoFile = File(it)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.dicoding.nutriseeapp.fileprovider",
                    photoFile
                )
                startUCrop(photoUri)
            }
        }
    }

    private val chooseFromGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val photoUri = result.data?.data
            if (photoUri != null) {
                startUCrop(photoUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        btnTakePhoto = view.findViewById(R.id.cameraButton)
        btnChooseFromGallery = view.findViewById(R.id.galleryButton)
        imgPreview = view.findViewById(R.id.previewImageView)

        btnTakePhoto.setOnClickListener {
            takePhoto()
        }

        btnChooseFromGallery.setOnClickListener {
            chooseFromGallery()
        }

        return view
    }

    private fun takePhoto() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Toast.makeText(requireContext(), "Error occurred while creating the file", Toast.LENGTH_SHORT).show()
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.dicoding.nutriseeapp.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePhotoLauncher.launch(intent)
            }
        }
    }

    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        chooseFromGalleryLauncher.launch(intent)
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = requireActivity().getExternalFilesDir(null)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun startUCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "IMG_CROPPED.jpg"))
        val options = UCrop.Options()
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(450, 450)
            .withOptions(options)
            .start(requireContext(), this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            imgPreview.setImageURI(resultUri) // Menampilkan gambar setelah proses crop
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
