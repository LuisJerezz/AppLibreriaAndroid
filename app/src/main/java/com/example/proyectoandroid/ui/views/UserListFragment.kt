package com.example.proyectoandroid.ui.views.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.FragmentUsuarioBinding
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.ui.adapter.UserAdapter
import com.example.proyectoandroid.ui.modelview.UserViewModel
import com.example.proyectoandroid.utils.ImageUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private var _binding: FragmentUsuarioBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    // Variables para manejar la imagen
    private var currentPhotoUri: Uri? = null
    private var base64Image: String? = null
    private var dialogView: View? = null // Variable para almacenar la vista del diálogo
    private var imagePreview: ImageView? = null // Variable para la vista previa de la imagen

    // Códigos de solicitud
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddButton()
        setupRecyclerView()
        observeViewModel()
        viewModel.getUsers()
    }

    private fun setupAddButton() {
        binding.btnAddUser.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_user, null)
        val editTextName = dialogView?.findViewById<TextInputEditText>(R.id.editTextNewName)
        val editTextEmail = dialogView?.findViewById<TextInputEditText>(R.id.editTextNewEmail)
        val textInputLayoutEmail = dialogView?.findViewById<TextInputLayout>(R.id.textInputLayoutEmail)
        imagePreview = dialogView?.findViewById<ImageView>(R.id.imagePreview) // Inicializar imagePreview
        val btnCamera = dialogView?.findViewById<Button>(R.id.btnCamera)
        val btnGallery = dialogView?.findViewById<Button>(R.id.btnGallery)



        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Nuevo Usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()



        // Manejar la selección de imagen desde la cámara
        btnCamera?.setOnClickListener {
            checkCameraPermission()
        }

        // Manejar la selección de imagen desde la galería
        btnGallery?.setOnClickListener {
            checkGalleryPermission()
        }

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val name = editTextName?.text.toString().trim()
                val email = editTextEmail?.text.toString().trim()

                when {
                    name.isEmpty() -> editTextName?.error = "Nombre requerido"
                    email.isEmpty() -> {
                        textInputLayoutEmail?.error = "Email requerido"
                    }
                    !isValidEmail(email) -> {
                        textInputLayoutEmail?.error = "Formato de email inválido"
                    }
                    else -> {
                        // Crear usuario con la imagen en Base64 o una imagen por defecto
                        val newUser = User(
                            id = generateNewId(),
                            nombre = name,
                            email = email,
                            imagen = base64Image ?: "https://picsum.photos/200/300?image=${generateNewId()}"
                        )
                        viewModel.addUser(newUser)
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

    // Método para convertir Bitmap a Base64
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Método para verificar permisos de la cámara
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
        } else {
            openCamera()
        }
    }

    // Método para verificar permisos de la galería
    private fun checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_IMAGE_GALLERY)
        } else {
            openGallery()
        }
    }

    // Método para abrir la cámara
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        currentPhotoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    // Método para abrir la galería
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    // Método para crear un archivo de imagen temporal
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    // Manejar el resultado de la cámara o galería
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> handleCameraImage()
                REQUEST_IMAGE_GALLERY -> handleGalleryImage(data)
            }
        }
    }

    private fun handleCameraImage() {
        currentPhotoUri?.let { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            base64Image = ImageUtils.bitmapToBase64(bitmap) // 🔹 Usamos ImageUtils
            imagePreview?.setImageBitmap(bitmap)
        }
    }

    private fun handleGalleryImage(data: Intent?) {
        val selectedImageUri = data?.data
        selectedImageUri?.let { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            base64Image = ImageUtils.bitmapToBase64(bitmap) // 🔹 Usamos ImageUtils
            imagePreview?.setImageBitmap(bitmap)
        }
    }


    // Método para validar el formato del email
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Método para generar un ID único
    private fun generateNewId(): Int {
        return (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    }

    private fun setupRecyclerView() {
        Log.d("UserListFragment", "Configurando RecyclerView en el Fragmento")

        userAdapter = UserAdapter(
            onEditClick = { user ->
                Log.d("UserListFragment", "Recibido onEditClick para usuario: ${user.nombre}")
                showEditDialog(user)
            },
            onDeleteClick = { user ->
                viewModel.delUser(user.id)
            }
        )

        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.userLiveData.observe(viewLifecycleOwner) { users ->
            userAdapter.updateList(users)
        }
    }

    private fun showEditDialog(user: User) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_user, null)
        val editTextName = dialogView.findViewById<TextInputEditText>(R.id.editTextName)
        val editTextEmail = dialogView.findViewById<TextInputEditText>(R.id.editTextEmail)
        val textInputLayoutEmail = dialogView.findViewById<TextInputLayout>(R.id.textInputLayoutEmail)

        editTextName.setText(user.nombre)
        editTextEmail.setText(user.email)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar Usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val updatedName = editTextName.text.toString().trim()
                val updatedEmail = editTextEmail.text.toString().trim()

                when {
                    updatedName.isEmpty() -> editTextName.error = "Nombre requerido"
                    updatedEmail.isEmpty() -> {
                        textInputLayoutEmail.error = "Email requerido"
                    }
                    !isValidEmail(updatedEmail) -> {
                        textInputLayoutEmail.error = "Formato de email inválido"
                    }
                    else -> {
                        val updatedUser = user.copy(nombre = updatedName, email = updatedEmail)
                        viewModel.editUser(user, updatedUser)
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}