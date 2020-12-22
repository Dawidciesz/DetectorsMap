import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiesta.detector.R
import com.fiesta.detector.utility.ListItems

class PoiListAdapter(private val dataSet: ArrayList<ListItems>) :
    RecyclerView.Adapter<PoiListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView
        val name: TextView
        val description: TextView

        init {
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.itemImage)
            name = view.findViewById(R.id.itemName)
            description = view.findViewById(R.id.itemDescription)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.image.setImageDrawable() = dataSet[position].getImage
        viewHolder.name.text = dataSet[position].name
        viewHolder.description.text = dataSet[position].description
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
