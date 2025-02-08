package com.example.proyectoandroid.ui.modelview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoandroid.data.repository.InMemoryUserRepository
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.usecase.GetUsersByUsernameUseCase
import com.example.proyectoandroid.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {
    var userListLiveData = MutableLiveData<List<User>>()
    var progressBarLiveData = MutableLiveData<Boolean>()
    var search = MutableLiveData<String>()
    val repository = InMemoryUserRepository()
    val useCaseList = GetUsersUseCase(repository)
    val useCaseUsernameList = GetUsersByUsernameUseCase(repository)


    fun list(){
        viewModelScope.launch {
            progressBarLiveData.value=true
            delay(2000)
            var data :List<User>?
            withContext(Dispatchers.IO) {
                data = useCaseList()


                if (data != null)
                    userListLiveData.value = data!!
                progressBarLiveData.value = false
            }
        }
    }

    fun searchByUsername(username : String){
        search.value = username
    }

    fun listForUsername(username : String){
        viewModelScope.launch {
            progressBarLiveData.value = true
            delay(2000)
            var data : List<User>?
            withContext(Dispatchers.IO){
                useCaseUsernameList.username = username
                data = useCaseUsernameList()

            }

            if (data != null){
                userListLiveData.value = data!!
                progressBarLiveData.value = false

            }
        }
    }

}