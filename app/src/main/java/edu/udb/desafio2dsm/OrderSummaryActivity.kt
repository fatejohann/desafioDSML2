package edu.udb.desafio2dsm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrderSummaryActivity : AppCompatActivity() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalAmountTextView: TextView
    private lateinit var checkoutButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference // Obtiene la referencia a la base de datos

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalAmountTextView = findViewById(R.id.totalAmountTextView)
        checkoutButton = findViewById(R.id.checkoutButton)

        // Obtén los artículos del carrito desde CartManager
        val cartItems = CartManager.getCartItems()

        // Configura el RecyclerView
        cartAdapter = CartAdapter(cartItems)
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Calcula y muestra el total
        val totalAmount = cartItems.sumOf { it.price * it.quantity }
        totalAmountTextView.text = "Total: $" + String.format("%.2f", totalAmount)

        // Manejar el botón de finalizar compra
        checkoutButton.setOnClickListener {
            val userId = auth.currentUser?.uid

            if (userId != null) {
                // Crea un mapa de datos del carrito
                val cartData = cartItems.map { item ->
                    mapOf(
                        "name" to item.title,
                        "price" to item.price,
                        "quantity" to item.quantity
                    )
                }

                // Guarda los datos del carrito en Firebase
                val orderId = database.child("orders").push().key // Crea una nueva clave para el pedido
                val orderUpdates = mapOf(
                    "userId" to userId, // Asociar el pedido con el ID del usuario
                    "totalAmount" to totalAmount,
                    "items" to cartData
                )

                if (orderId != null) {
                    database.child("orders").child(orderId).setValue(orderUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Limpia el carrito después de la compra
                                CartManager.clearCart()
                                Toast.makeText(this, "Compra finalizada y datos guardados", Toast.LENGTH_SHORT).show()
                                // Navega a la pantalla principal o confirma la compra
                                finish()
                            } else {
                                Toast.makeText(this, "Error al guardar los datos de la compra", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Error al crear el pedido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
