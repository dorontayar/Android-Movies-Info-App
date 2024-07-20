package il.ac.hit.android_movies_info_app.ui.movie_detail.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import il.ac.hit.android_movies_info_app.data.model.movie_search_detailed.Image
import il.ac.hit.android_movies_info_app.databinding.SlideItemBinding
import il.ac.hit.android_movies_info_app.utils.Constants

class GalleryAdapter(private val images: List<Image>) :
    RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {

    class ImageViewHolder(private val itemBinding: SlideItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        private lateinit var image: Image

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: Image) {
            this.image = item
            val imagePath: String = Constants.IMAGE_TYPE_ORIGINAL + item.filePath
            Picasso.get().load(imagePath).into(itemBinding.imageMovie)
        }

        override fun onClick(v: View?) {
            // Implement your click logic if needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = SlideItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}
