package com.example.proyectoandroid.ui.modelview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.usecase.AddUserUseCase
import com.example.proyectoandroid.domain.usecase.DeleteUserUseCase
import com.example.proyectoandroid.domain.usecase.EditUserUseCase
import com.example.proyectoandroid.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {

    val userLiveData = MutableLiveData<List<User>>()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()

    fun getUsers() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            try {
                val data = getUsersUseCase()
                userLiveData.postValue(data!!)
            } catch (e: Exception) {
                errorLiveData.value = "ERROR EN LA CORRUTINA DE GET"
            } finally {
                progressBarLiveData.value = false
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                addUserUseCase(user)
                getUsers()
            } catch (e: Exception) {
                errorLiveData.value = "ERROR EN LA CORRUTINA DE ADD"
            }
        }
    }

    fun delUser(id: Int) {
        Log.d("UserViewModel", "Llamando a delUser con ID: $id")

        viewModelScope.launch {
            try {
                val success = deleteUserUseCase(id)
                if (success) {
                    val updatedUsers = userLiveData.value?.filter { it.id != id } ?: emptyList()
                    userLiveData.postValue(updatedUsers) // 🔹 Actualiza LiveData sin recargar datos
                    Log.d("UserViewModel", "Usuario eliminado correctamente y LiveData actualizado")
                } else {
                    Log.e("UserViewModel", "Error: Usuario con ID $id no encontrado")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "ERROR EN LA CORRUTINA DE DEL: ${e.message}")
            }
        }
    }





    fun editUser(oldUser: User, newUser: User) {
        Log.d("UserViewModel", "Editando usuario: ${oldUser.nombre} -> ${newUser.nombre}")
        viewModelScope.launch {
            try {
                editUserUseCase(oldUser, newUser)

                // 🔹 Actualizar solo el usuario modificado en el LiveData sin recargar toda la lista
                val updatedList = userLiveData.value?.map { if (it.id == oldUser.id) newUser else it }
                userLiveData.postValue(updatedList!!)

            } catch (e: Exception) {
                Log.e("UserViewModel", "Error al editar usuario", e)
                errorLiveData.postValue("ERROR EN LA CORRUTINA DE EDIT")
            }
        }
    }


}