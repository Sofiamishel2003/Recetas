package com.example.recetas
/**
 * Nombre: Sofía Mishell Velasquez
 * Carnet: 22049
 * Proyecto: Laboratorio4
 * Materia: Programación de plataformas moviles
 * Archivo: MainActivity.kt
 * Descripción: aplicación que muestra un recetario con imagen, se puede agregar nuevas recetas, eliminar recetas y no se pueden repetir
 */
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.recetas.ui.theme.RecetasTheme
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecetasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

data class Elemento(val nombre: String, val urlImagen: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyUI() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Recetas de la Abuela") }
    )
}
//Funcion general/principal: contiene los textfields(inputs) y el boton de agregar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    MyUI() //agrega la función de barra que contiene el titulo
    val listaElementos = remember { mutableStateListOf<Elemento>() }
    val nombreReceta = remember { mutableStateOf("") }
    val urlImagen = remember { mutableStateOf("") }
    val recetaRepetida = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.padding(100.dp),
    ) {
        Column {
            TextField(
                value = nombreReceta.value,
                onValueChange = { nombreReceta.value = it },
                label = { Text("Nombre de la receta") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = urlImagen.value,
                onValueChange = { urlImagen.value = it },
                label = { Text("URL de la imagen") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    //comprobar que ambos textfields no estén vacios
                    if (nombreReceta.value.isNotBlank() && urlImagen.value.isNotBlank()) {
                        // Comprobar si la receta ya existe
                        if (listaElementos.any { it.nombre == nombreReceta.value }) {
                            // si ya existe saca una bandera llamada receta repetida
                            recetaRepetida.value = true
                        } else {
                            //si no existe la agrega
                            listaElementos.add(Elemento(nombreReceta.value, urlImagen.value))
                            nombreReceta.value = ""
                            urlImagen.value = ""
                            recetaRepetida.value = false
                        }
                    }
                }
            ) {
                Text("Agregar")
            }
            if (recetaRepetida.value) {
                //si la receta repetida se vuelve true, saca un mensaje que ya existe
                Text("La receta ya existe!", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(listaElementos) { elemento ->
                    TarjetaReceta(elemento) {
                        listaElementos.remove(elemento)
                    }
                }
            }
        }
    }
}
//Funcion de cartas que agrega las recets en su formato imagen-informacióbn
@Composable
fun TarjetaReceta(elemento: Elemento, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onRemove() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(data = elemento.urlImagen),
                contentDescription = "Imagen de ${elemento.nombre}",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = elemento.nombre,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecetasTheme {
        Greeting("Android")
    }
}
