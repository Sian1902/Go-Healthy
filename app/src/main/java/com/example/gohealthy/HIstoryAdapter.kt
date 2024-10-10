import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gohealthy.HistoryItem
import com.example.gohealthy.databinding.HistoryItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class HistoryAdapter(
    private val historyList: MutableList<HistoryItem>,
    private val userId: String
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.Date.text = item.date
            binding.KcalIn.text = item.kcalIn.toString()
            binding.KcalOut.text = item.kcalOut.toString()
            binding.Steps.text = item.steps.toString()

            // Set up the delete button's OnClickListener
            binding.deleteButton.setOnClickListener {
                // Call the delete method with the document ID
                deleteHistoryItem(item)
            }
        }

        private fun deleteHistoryItem(item: HistoryItem) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(userId)
                .collection("history")
                .document(item.id) // Assuming you have a unique ID for each HistoryItem
                .delete()
                .addOnSuccessListener {
                    // Remove the item from the local list
                    historyList.remove(item)
                    Toast.makeText(binding.root.context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                    // Notify the adapter that an item has been removed
                    notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Log.w("HistoryAdapter", "Error deleting document", e)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = historyList.size
}
