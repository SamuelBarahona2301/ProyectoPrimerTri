package com.example.eventhub;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventhub.databinding.ActivityMainPageBinding;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    ArrayList<Evento> listaDatos;

    RecyclerView recycler;

    private AppBarConfiguration mAppBarConfiguration;
private ActivityMainPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMainPageBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainPage.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        listaDatos = new ArrayList<>();
        recycler = findViewById(R.id.recylcerId);
        // recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setLayoutManager(new GridLayoutManager(this, 2));

        llenarEventos();

        AdapterDatos adapter = new AdapterDatos(listaDatos);
        recycler.setAdapter(adapter);
    }

    private void llenarEventos() {
        listaDatos.add(new Evento(R.drawable.concierto, "Concierto en Vivo: Artista XYZ", "Concierto en vivo del reconocido artista XYZ. Un espectáculo lleno de energía y emoción", "Teatro Municipal", "20-07-2024", "20:00"));
        listaDatos.add(new Evento(R.drawable.arte, "Exposición de Arte Moderno", "Obras de artistas contemporáneos en esta emocionante exposición de arte moderno.", "Galería de Arte Contemporáneo", "25-03-2024", "10:00"));
        listaDatos.add(new Evento(R.drawable.gastronomia, "Festival de Comida Internacional", "Amplia variedad de delicias culinarias de todo el mundo en este festival gastronómico", "Plaza Principal", "02-04-2024", "18:00"));
        listaDatos.add(new Evento(R.drawable.teatro, "Presentación de Teatro Clásico: 'Romeo y Julieta'", "Revive la historia de amor más famosa de todos los tiempos con esta emocionante presentación", "Teatro Nacional", "10-04-2024", "19:30"));
        listaDatos.add(new Evento(R.drawable.libros, "Feria de Libros y Lectura", "Amplia selección de libros de diferentes géneros y participa en sesiones de lectura y charlas con autores destacados.", "Parque Retiro", "15-04-2024", "11:00"));
        listaDatos.add(new Evento(R.drawable.magia, "Espectáculo de Magia: Ilusionista Max", "Déjate sorprender por los trucos y la magia del ilusionista Max en este espectáculo lleno de misterio y diversión.", "Auditorio Municipal", "15-06-2024", "20:00"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}