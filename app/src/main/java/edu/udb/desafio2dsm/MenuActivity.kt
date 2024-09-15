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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    private var cartItemCount: Int = 0
    private lateinit var cartItemCountView: TextView
    private lateinit var userNameTextView: TextView // TextView para mostrar el nombre del usuario
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencia del TextView que muestra el número de items en el carrito
        cartItemCountView = findViewById(R.id.cartItemCount)
        userNameTextView = findViewById(R.id.userNameTextView) // Referenciar el TextView del nombre del usuario

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

        // Mostrar el nombre del usuario en el TextView
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val displayName = currentUser.displayName  // Obtener el nombre del usuario
            if (!displayName.isNullOrEmpty()) {
                userNameTextView.text = "Bienvenido: $displayName"
            } else {
                userNameTextView.text = "Bienvenido"
            }
        } else {
            userNameTextView.text = "Bienvenido"
        }


        // Maneja el click en el ícono del carrito
        val cartIcon: ImageView = findViewById(R.id.cartIcon)
        cartIcon.setOnClickListener {
            openCartActivity()
        }
    }

    private fun addToCart(menuItem: MenuItem) {
        cartItemCount++
        cartItemCountView.text = cartItemCount.toString()
        CartManager.addItem(menuItem)  // Agregar el ítem al carrito
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
                openCartActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


data class MenuItem(val title: String,
                    val description: String,
                    val imageResId: Int)

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val addToCartCallback: (MenuItem) -> Unit
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
            addToCartCallback(menuItem)
            Toast.makeText(holder.itemView.context, "Añadido ${menuItem.title} al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = menuItems.size
}
