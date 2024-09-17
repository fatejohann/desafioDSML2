package edu.udb.desafio2dsm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        val menuItems = listOf(
            MenuItem("Ibuprofeno", "Analgésico", R.mipmap.ibuprofeno, 8.99),
            MenuItem("Tums", "Antiácidos", R.mipmap.tums, 5.49),
            MenuItem("Multivitamínico diario", "Vitaminas", R.drawable.multivitaminico_foreground, 12.99),
            MenuItem("Band-Aid", "Vendajes adhesivos", R.drawable.bandaid_foreground, 3.99),
            MenuItem("Purell", "Desinfectante de manos", R.drawable.purell_foreground, 2.99),
            MenuItem("Neutrogena SPF 50", "Protector solar", R.drawable.neutrogena_foreground, 10.99),
            MenuItem("Listerine", "Enjuague bucal", R.drawable.listerine_foreground, 6.99),
            MenuItem("Cetirizina", "Antialérgicos", R.drawable.cetirizina_foreground, 15.99),
            MenuItem("Robitussin", "Jarabe para la tos", R.drawable.robitussin_foreground, 7.99),
            MenuItem("Termómetro Vicks", "Termómetro digital", R.drawable.thermometer_foreground, 14.99)
        )



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

        cartIcon.setOnClickListener {
            val intent = Intent(this, OrderSummaryActivity::class.java)
            startActivity(intent)
        }

        // Maneja el clic en el botón para ver el historial
        findViewById<Button>(R.id.viewHistoryButton).setOnClickListener {
            val intent = Intent(this, PurchaseHistoryActivity::class.java)
            startActivity(intent)
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


data class MenuItem(
    val title: String,
    val description: String,
    val imageResId: Int,
    val price: Double, // Añadir campo de precio
    val quantity: Int = 1 // Añadir campo de cantidad por defecto
)



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
        holder.itemView.findViewById<TextView>(R.id.itemPrice).text = "$${menuItem.price}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("productName", menuItem.title)
                putExtra("productPrice", menuItem.price)
                putExtra("productImageResId", menuItem.imageResId)
            }
            context.startActivity(intent)
        }

    }


    override fun getItemCount() = menuItems.size
}
