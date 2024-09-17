package edu.udb.desafio2dsm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PurchaseHistoryAdapter : ListAdapter<Order, PurchaseHistoryAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderDateTextView: TextView = itemView.findViewById(R.id.orderDateTextView)
        private val totalAmountTextView: TextView = itemView.findViewById(R.id.totalAmountTextView)

        fun bind(order: Order) {
            orderDateTextView.text = order.date
            totalAmountTextView.text = "Total: $${order.totalAmount}"

            // Opcional: Mostrar los ítems de la orden
            // Puedes agregar un RecyclerView dentro de este ViewHolder si deseas mostrar los ítems
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}


data class Order(
    val orderId: String = "",
    val userId: String = "",
    val date: String = "",
    val totalAmount: Double = 0.0,
    val items: List<Item> = emptyList() // Cambiado a List<Item>
)

data class Item(
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0
)




