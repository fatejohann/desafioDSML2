package edu.udb.desafio2dsm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productImageView: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productQuantityEditText: EditText
    private lateinit var addToCartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Obtener los elementos del layout
        productImageView = findViewById(R.id.productImageView)
        productNameTextView = findViewById(R.id.productNameTextView)
        productPriceTextView = findViewById(R.id.productPriceTextView)
        productQuantityEditText = findViewById(R.id.productQuantityEditText)
        addToCartButton = findViewById(R.id.addToCartButton)

        // Obtener los datos del Intent
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)
        val productImageResId = intent.getIntExtra("productImageResId", R.drawable.ic_launcher_foreground)

        // Configurar los datos en la vista
        productNameTextView.text = productName
        productPriceTextView.text = "Precio: $$productPrice"
        productImageView.setImageResource(productImageResId)

        // Manejar el evento de clic del botón "Agregar al carrito"
        addToCartButton.setOnClickListener {
            val quantity = productQuantityEditText.text.toString().toIntOrNull()
            if (quantity != null && quantity > 0) {
                CartManager.addItem(MenuItem(productName ?: "", "", productImageResId, productPrice, quantity))
                Toast.makeText(this, "$productName añadido al carrito", Toast.LENGTH_SHORT).show()
                finish()  // Cerrar la actividad y volver al menú
            } else {
                Toast.makeText(this, "Por favor, ingresa una cantidad válida", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
