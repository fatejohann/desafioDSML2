package edu.udb.desafio2dsm

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PurchaseHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var purchaseHistoryAdapter: PurchaseHistoryAdapter
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)

        recyclerView = findViewById(R.id.purchaseHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        purchaseHistoryAdapter = PurchaseHistoryAdapter()
        recyclerView.adapter = purchaseHistoryAdapter

        database = FirebaseDatabase.getInstance().reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        fetchPurchaseHistory()
    }

    private fun fetchPurchaseHistory() {
        database.child("orders").orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orders = mutableListOf<Order>()
                    for (data in snapshot.children) {
                        val order = data.getValue(Order::class.java)
                        if (order != null) {
                            orders.add(order)
                        }
                    }
                    purchaseHistoryAdapter.submitList(orders)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores
                }
            })
    }

}

