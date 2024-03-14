package com.example.eventhub;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventhub.databinding.ActivityMainPageBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicReference;

public class MainPage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
private ActivityMainPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainPage.toolbar);
        AtomicReference<DrawerLayout> drawer = new AtomicReference<>(binding.drawerLayout);
        NavigationView navigationView = binding.navView;

        // Configuración inicial del AppBarConfiguration y NavController omitida...

        // Añadir el listener de selección de items aquí
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Aquí puedes manejar la acción de volver al "Home". Si ya estás usando un NavController,
                // puede que sólo necesites hacer popBackStack hasta el destino de inicio, o navegar hacia él.
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);
                navController.navigate(R.id.nav_home);
            } else if (id == R.id.nav_perfil) {
                // Navegar hacia ProfileActivity
                Intent intent = new Intent(MainPage.this, ProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                // Manejar el cierre de sesión
                FirebaseAuth.getInstance().signOut();
                // Navegar de vuelta a LoginActivity
                Intent intent = new Intent(MainPage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            // Cerrar el drawer
            drawer.set(findViewById(R.id.drawer_layout));
            drawer.get().closeDrawer(GravityCompat.START);
            return true;
        });
    }

}