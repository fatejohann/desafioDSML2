package edu.udb.desafio2dsm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrderSummaryActivity : AppCompatActivity() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalAmountTextView: TextView
    private lateinit var checkoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

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
            // Aquí puedes manejar la finalización de la compra
            Toast.makeText(this, "Compra finalizada", Toast.LENGTH_SHORT).show()
        }
    }
}
