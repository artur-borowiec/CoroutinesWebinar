package pl.akademiaandroida.rickandmorty

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val api by lazy { createApiService() }
    private val adapter by lazy { CharacterAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecycler()
        getCharacters()
    }

    private fun initRecycler() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    private fun getCharacters() {
        api.getCharacters().enqueue(object : Callback<CharactersResponse> {
            override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<CharactersResponse>,
                response: Response<CharactersResponse>
            ) {
                if (!response.isSuccessful) {
                    runOnUiThread { showErrorMessage(response) }
                }

                response.body()?.let {
                    runOnUiThread { showCharacters(it.results) }
                }
            }
        })
    }

    private fun showErrorMessage(response: Response<CharactersResponse>) {
        Toast.makeText(
            this,
            "An error occurred, code: ${response.code()}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showCharacters(characters: List<Character>) {
        adapter.setCharacters(characters)
    }

    private fun createApiService(): RickAndMortyAPI {
        val url = "https://rickandmortyapi.com/api/"

        val interceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(RickAndMortyAPI::class.java)
    }
}
