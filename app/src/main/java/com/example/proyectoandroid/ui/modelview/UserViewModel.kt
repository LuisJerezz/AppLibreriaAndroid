package com.example.proyectoandroid.ui.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.models.UserCreateRequest
import com.example.proyectoandroid.domain.models.UserUpdateRequest
import com.example.proyectoandroid.domain.usecase.AddUserUseCase
import com.example.proyectoandroid.domain.usecase.DeleteUserUseCase
import com.example.proyectoandroid.domain.usecase.EditUserUseCase
import com.example.proyectoandroid.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {

    private val _userLiveData = MutableLiveData<List<User>>()
    val userLiveData : LiveData<List<User>> get() = _userLiveData

    private val _progressBarLiveData = MutableLiveData<Boolean>()
    val progressBarLiveData : LiveData<Boolean> get() = _progressBarLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData : LiveData<String> get() = _errorLiveData

    init {
        getUsers()
    }

    fun getUsers() {
        _progressBarLiveData.value = true
        viewModelScope.launch {
            try {
                _userLiveData.value = getUsersUseCase.execute()
            }catch (e : Exception){
                _errorLiveData.value = e.message ?: "Error desconocido AAAAAAAAA"
            }finally {
                _progressBarLiveData.value = false
            }
        }
    }

    fun addUser(request: UserCreateRequest) {
        viewModelScope.launch {
            try {
                addUserUseCase.execute(request)
                getUsers()
            } catch (e: Exception) {
                _errorLiveData.value = "Error añadiendo: ${e.message}"
            }
        }
    }

    fun deleteUser(id : String){
        viewModelScope.launch {
            try {
                deleteUserUseCase.execute(id)
                getUsers()
            }catch (e:Exception){
                _errorLiveData.value = e.message ?: "Error desconocido BORRANDO"
            }
        }
    }

    fun editUser(id: String, updateUser: UserUpdateRequest){
        viewModelScope.launch {
            try {
                editUserUseCase.execute(id, updateUser)
                getUsers()
            }catch (e : Exception){
                _errorLiveData.value = e.message ?: "Error desconocido EDITANDO"
            }
        }
    }


    //val userLiveData = MutableLiveData<List<User>>()
    //val progressBarLiveData = MutableLiveData<Boolean>()
    //val errorLiveData = MutableLiveData<String>()
//
    //fun getUsers() {
    //    progressBarLiveData.value = true
    //    viewModelScope.launch {
    //        try {
    //            val data = getUsersUseCase()
    //            userLiveData.postValue(data!!)
    //        } catch (e: Exception) {
    //            errorLiveData.value = "ERROR EN LA CORRUTINA DE GET"
    //        } finally {
    //            progressBarLiveData.value = false
    //        }
    //    }
    //}
//
    //fun addUser(user: User) {
    //    viewModelScope.launch {
    //        try {
    //            addUserUseCase(user)
    //            getUsers()
    //        } catch (e: Exception) {
    //            errorLiveData.value = "ERROR EN LA CORRUTINA DE ADD"
    //        }
    //    }
    //}
//
    //fun delUser(id: Int) {
    //    Log.d("UserViewModel", "Llamando a delUser con ID: $id")
//
    //    viewModelScope.launch {
    //        try {
    //            val success = deleteUserUseCase(id)
    //            if (success) {
    //                val updatedUsers = userLiveData.value?.filter { it.id != id } ?: emptyList()
    //                userLiveData.postValue(updatedUsers) // 🔹 Actualiza LiveData sin recargar datos
    //                Log.d("UserViewModel", "Usuario eliminado correctamente y LiveData actualizado")
    //            } else {
    //                Log.e("UserViewModel", "Error: Usuario con ID $id no encontrado")
    //            }
    //        } catch (e: Exception) {
    //            Log.e("UserViewModel", "ERROR EN LA CORRUTINA DE DEL: ${e.message}")
    //        }
    //    }
    //}
//
//
//
//
//
    //fun editUser(oldUser: User, newUser: User) {
    //    Log.d("UserViewModel", "Editando usuario: ${oldUser.nombre} -> ${newUser.nombre}")
    //    viewModelScope.launch {
    //        try {
    //            editUserUseCase(oldUser, newUser)
//
    //            // 🔹 Actualizar solo el usuario modificado en el LiveData sin recargar toda la lista
    //            val updatedList = userLiveData.value?.map { if (it.id == oldUser.id) newUser else it }
    //            userLiveData.postValue(updatedList!!)
//
    //        } catch (e: Exception) {
    //            Log.e("UserViewModel", "Error al editar usuario", e)
    //            errorLiveData.postValue("ERROR EN LA CORRUTINA DE EDIT")
    //        }
    //    }
    //}
//

}