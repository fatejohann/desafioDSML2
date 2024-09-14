package edu.udb.desafio2dsm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    private var cartItemCount: Int = 0
    private lateinit var cartItemCountView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Referencia del TextView que muestra el número de items en el carrito
        cartItemCountView = findViewById(R.id.cartItemCount)

        val recyclerView: RecyclerView = findViewById(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val menuItems = List(10) { index ->
            MenuItem(
                "Ítem ${index + 1}",
                "Descripción del ítem ${index + 1}",
                R.drawable.ic_launcher_foreground
            )
        }

        val adapter = MenuAdapter(menuItems, this::addToCart)
        recyclerView.adapter = adapter

        // Maneja el click en el ícono del carrito
        val cartIcon: ImageView = findViewById(R.id.cartIcon)
        cartIcon.setOnClickListener {
            openCartActivity()
        }
    }

    private fun addToCart() {
        cartItemCount++
        cartItemCountView.text = cartItemCount.toString()
    }

    private fun openCartActivity() {
        // Abre la nueva actividad que muestra los items del carrito
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Inflar el menú con el ítem del carrito
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                // Maneja el clic en el ícono del carrito
                Toast.makeText(this, "Carrito de compras", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

data class MenuItem(val title: String, val description: String, val imageResId: Int)

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val addToCartCallback: () -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.itemDescription)
        val imageView: ImageView = itemView.findViewById(R.id.itemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.titleTextView.text = menuItem.title
        holder.descriptionTextView.text = menuItem.description
        holder.imageView.setImageResource(menuItem.imageResId)

        holder.itemView.setOnClickListener {
            // Llamamos a la función de agregar al carrito
            addToCartCallback()
            Toast.makeText(holder.itemView.context, "Añadido ${menuItem.title} al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = menuItems.size
}
