package edu.udb.desafio2dsm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView: RecyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val cartItems = CartManager.getCartItems()

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = CartAdapter(cartItems)
            recyclerView.adapter = adapter
        }
    }
}

class CartAdapter(private val cartItems: List<MenuItem>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // Clase ViewHolder que contiene las vistas de cada item del carrito
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val itemQuantity: TextView = itemView.findViewById(R.id.itemQuantity)
        val itemPrice: TextView = itemView.findViewById(R.id.itemUnitPrice)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemTotalPrice: TextView = itemView.findViewById(R.id.itemTotalPrice)

    }

    // Crear el ViewHolder inflando el layout del item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(itemView)
    }

    // Asignar los valores a las vistas dentro del ViewHolder
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]
        holder.itemTitle.text = currentItem.title
        holder.itemQuantity.text = "Cantidad: ${currentItem.quantity}"
        holder.itemPrice.text = "$${currentItem.price}"
        holder.itemTotalPrice.text = "$${currentItem.price * currentItem.quantity}"

        holder.itemImage.setImageResource(currentItem.imageResId)
    }

    // Devuelve el número total de items
    override fun getItemCount(): Int {
        return cartItems.size
    }
}



