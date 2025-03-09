package com.example.proyectoandroid.ui.views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.util.Base64
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.DialogUserBinding
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.models.UserCreateRequest
import com.example.proyectoandroid.domain.models.UserUpdateRequest
import java.io.ByteArrayOutputStream

class UserDialogFragmentCU : DialogFragment() {

    private lateinit var binding: DialogUserBinding
    private var currentUser: User? = null
    private var selectedImageUri: Uri? = null

    var onCreate: ((UserCreateRequest) -> Unit)? = null
    var onUpdate: ((UserUpdateRequest) -> Unit)? = null
    companion object {
        fun newInstance(user: User): UserDialogFragmentCU {
            val args = Bundle()
            args.putSerializable("user", user)
            val fragment = UserDialogFragmentCU()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUserBinding.inflate(LayoutInflater.from(requireContext()))
        currentUser = arguments?.getSerializable("user") as? User

        // Configuración inicial común
        binding.imagePreview.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.btnCamera.setOnClickListener { checkCameraPermission() }
        binding.btnGallery.setOnClickListener { checkMediaPermission() }

        currentUser?.let { user ->
            // Modo EDICIÓN
            with(binding) {
                editDni.setText(user.dni)
                editDni.isEnabled = false
                editDni.alpha = 0.6f  // Efecto visual de deshabilitado
                editDni.inputType = InputType.TYPE_NULL  // Bloquea entrada de texto

                editName.setText(user.name)
                editEmail.setText(user.email)
                editPassword.setText("")
                editPhone.setText(user.phone)

                // Carga de imagen
                user.image?.let { imageStr ->
                    if (imageStr.startsWith("data:image")) {
                        loadBase64Image(imageStr)
                    } else {
                        val resId = context?.resources?.getIdentifier(
                            imageStr,
                            "drawable",
                            context?.packageName
                        ) ?: R.drawable.ic_users
                        imagePreview.setImageResource(resId)
                    }
                } ?: run {
                    imagePreview.setImageResource(R.drawable.ic_users)
                }
            }
        } ?: run {
            // Modo CREACIÓN
            with(binding) {
                editDni.isEnabled = true
                editDni.alpha = 1f
                editDni.inputType = InputType.TYPE_CLASS_TEXT
                editDni.setText("")
                imagePreview.setImageResource(R.drawable.ic_users)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(if (currentUser == null) "Nuevo Usuario" else "Editar Usuario")
            .setPositiveButton("Guardar") { _, _ -> saveUser() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    private fun loadBase64Image(imageString: String) {
        try {
            val pureBase64 = imageString.substringAfter(",")
            val imageBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            binding.imagePreview.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            binding.imagePreview.setImageResource(R.drawable.ic_users)
        }
    }

    private fun saveUser() {
        // Obtener valores comunes
        val name = binding.editName.text.toString().trim()
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        val phone = binding.editPhone.text.toString().trim()

        // Validación común
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Manejo de la imagen
        val image = selectedImageUri?.let { uri ->
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                "data:image/jpeg;base64,${bitmapToBase64(bitmap)}"
            } catch (e: Exception) {
                null
            }
        } ?: currentUser?.image ?: "ic_users"

        when (currentUser) {
            // Modo EDICIÓN
            is User -> {
                val updateRequest = UserUpdateRequest(
                    name = name,
                    email = email,
                    password = password,
                    phone = phone,
                    image = image,
                    disponible = currentUser!!.disponible
                )

                // Validación específica para edición
                if (!isValidUpdate(updateRequest)) {
                    Toast.makeText(requireContext(), "Datos de actualización inválidos", Toast.LENGTH_SHORT).show()
                    return
                }

                onUpdate?.invoke(updateRequest)
            }

            // Modo CREACIÓN
            null -> {
                val dni = binding.editDni.text.toString().trim()

                if (!isValidDni(dni)) {
                    Toast.makeText(requireContext(), "DNI inválido", Toast.LENGTH_SHORT).show()
                    return
                }

                val createRequest = UserCreateRequest(
                    dni = dni,
                    name = name,
                    email = email,
                    password = password,
                    phone = phone,
                    image = image
                )

                onCreate?.invoke(createRequest)
            }
        }

        dismiss()
    }

    // Funciones de validación
    private fun isValidDni(dni: String): Boolean {
        val dniPattern = "^[0-9]{8}[A-Za-z]\$"
        return dni.matches(dniPattern.toRegex())
    }

    private fun isValidUpdate(updateRequest: UserUpdateRequest): Boolean {
        return updateRequest.email.contains("@") &&
                updateRequest.phone.length >= 9
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun loadImageFromResource(image: String) {
        val resId = context?.resources?.getIdentifier(image, "drawable", context?.packageName)
        resId?.let { binding.imagePreview.setImageResource(it) }
    }



    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let { uri -> binding.imagePreview.setImageURI(uri) }
        }
    }

    private fun openImageSelector() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        imagePickerLauncher.launch(intent)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) openCamera() else Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    private val requestMediaPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) openImageSelector() else Toast.makeText(requireContext(), "Permiso de acceso a medios denegado", Toast.LENGTH_SHORT).show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    private fun checkMediaPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openImageSelector()
            } else {
                requestMediaPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openImageSelector()
            } else {
                requestMediaPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            binding.imagePreview.setImageBitmap(imageBitmap)
            selectedImageUri = saveImageToGallery(imageBitmap)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun saveImageToGallery(bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream = requireContext().contentResolver.openOutputStream(it)
            outputStream?.use { stream -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) }
        }
        return uri
    }
}
