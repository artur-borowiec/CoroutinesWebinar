package pl.akademiaandroida.rickandmorty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val characters by lazy { mutableListOf<Character>() }

    fun setCharacters(characters: List<Character>) {
        if (characters.isNotEmpty()) {
            this.characters.clear()
        }

        this.characters.addAll(characters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_character, parent, false)

        return CharacterViewHolder(itemView)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(character: Character) {
            with(itemView) {
                Glide.with(this)
                    .load(character.image)
                    .into(imageView)

                textView.text = character.name
            }
        }
    }
}