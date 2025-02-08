package com.example.proyectoandroid.ui

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.ActivityMainBinding
import com.example.proyectoandroid.ui.modelview.UserViewModel
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.ui.adapter.UserAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: UserAdapter
    val userViewModel: UserViewModel by viewModels() //tiene que ser constante.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mySearch.setOnQueryTextListener(this)  //cargamos el listener
        registerLiveData()  //Observamos cambios.
        loadDada() //se cargan todos los datos.
        initRecyclerView()  //inicializamos el recyclerView.
        //test()
    }

    private fun loadDada() {
        userViewModel.list()  //simulamos un evento para iniciar la carga de datos desde el viewmodel

    }

    /*
    Quiero suscribir al activity, cuando los datos de dogListLiveData,
    cambién. En el momento que haya ese cambio, el ViewModel notificará
    al activity y ejecutará la lambda.
     */
    private fun registerLiveData() {
        userViewModel.userListLiveData.observe(
            this, {  myList->
                //Aquí hacemos la actualización del adapter.
                adapter.userRepository = myList!!  //aseguro los datos.
                binding.myRecyclerPpal.adapter = adapter  //le asigno el adapter.
                adapter.notifyDataSetChanged()  //No hace falta, pero por si acaso.
            })

        /*
        Cuando exista un cambio en el modelo, quiero que el Activity
        sea notificado para que actualize la ui.
         */
        userViewModel.progressBarLiveData.observe(
            this, { visible ->
                binding.progressBar.isVisible = visible
                Log.i("TAG-DOGS","ProgressBar esta $visible")            }
        )

        /*
        Observamos un cambio en el search.
         */
        userViewModel.search.observe(  //el campo search, ha cambiado
            this, { bread->
                userViewModel.listForUsername(bread)  //cambiamos los datos.
                hideKeyBoard()
            }
        )
    }

    /*
    Pone todo en funcionamiento
     */
    private fun initRecyclerView(){
        binding.myRecyclerPpal.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
    }


    /*
    Este método, es llamado cuando se escribe algo en el campo y se pulsa.
     */
    override fun onQueryTextSubmit(query: String?): Boolean {

        if (!query.isNullOrEmpty())
            userViewModel.searchByUsername(query!!)
        return true
    }

    /*
    Cualquier cambio, llamará a este método. Estoy obligado a ponerlo
    Principalmente, lo utilizo para cargar toda la lista de nuevo, al
    estar el campo vacío.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty()) {
            userViewModel.list()
            hideKeyBoard()
        }
        return true
    }



    /*
    Método que cierra el teclado. MUY INTERESANTE...
     */
    private fun hideKeyBoard() {
        val imn = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(binding.myLayoutPpal.windowToken, 0)
    }

    private fun test() {
        //  TestApi.testDogApi()
    }
}




    //-------------------------------------------
    //          ANTES DEL MVVM
    //-------------------------------------------

    //private lateinit var binding: ActivityMainDrawerBinding
    //lateinit var navController: NavController
//
    //override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
//
    //    // Inflar el layout que contiene el DrawerLayout y el Toolbar
    //    binding = ActivityMainDrawerBinding.inflate(layoutInflater)
    //    setContentView(binding.root)
//
    //    // Configurar el Toolbar como ActionBar
    //    setSupportActionBar(binding.toolbar)
//
    //    // Configurar Navigation Drawer
    //    val drawerLayout: DrawerLayout = binding.drawerLayout
    //    val navView: NavigationView = binding.navView
//
    //    // Configurar NavController desde el NavHostFragment
    //    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    //    navController = navHostFragment.navController
//
    //    // Configurar el Drawer con NavController
    //    NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    //    NavigationUI.setupWithNavController(navView, navController)
//
    //    // Escuchar la selección de los items del menú del NavigationView
    //    navView.setNavigationItemSelectedListener { menuItem ->
    //        when (menuItem.itemId) {
    //            R.id.nav_ads_list -> {
    //                // Navegar al fragmento de anuncios
    //                navController.navigate(R.id.nav_ads_list)
    //            }
    //            R.id.nav_user_list -> {
    //                // Navegar al fragmento de usuarios
    //                navController.navigate(R.id.nav_user_list)
    //            }
    //            R.id.nav_profile -> {
    //                // Navegar al fragmento de perfil
    //                navController.navigate(R.id.nav_profile)
    //            }
    //            R.id.nav_settings -> {
    //                // Navegar al fragmento de ajustes
    //                navController.navigate(R.id.nav_settings)
    //            }
    //        }
    //        // Cerrar el Drawer después de la selección
    //        drawerLayout.closeDrawers()
    //        true
    //    }
    //}
//
    //override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //    // Inflar el menú en la Toolbar
    //    menuInflater.inflate(R.menu.main_menu, menu)
    //    return true
    //}
//
    //override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //    // Gestionar los clics en las opciones de la Toolbar
    //    return when (item.itemId) {
    //        R.id.action_logout -> {
//
    //            true
    //        }
    //        R.id.action_settings -> {
    //            // Navegar a ajustes
    //            navController.navigate(R.id.nav_settings)
    //            true
    //        }
    //        R.id.action_help -> {
    //            // Navegar a ayuda
    //            navController.navigate(R.id.nav_help) // Asegúrate de que tengas este fragmento
    //            true
    //        }
    //        else -> super.onOptionsItemSelected(item)
    //    }
    //}
//
//
//
    //override fun onSupportNavigateUp(): Boolean {
    //    return NavigationUI.navigateUp(navController, binding.drawerLayout)
    //}


//}
