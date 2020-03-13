package com.example.movielist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movielist.R
import com.example.movielist.model.Movie
import com.example.movielist.util.Constants

class MoviesAdapter(
    private val requestManager: RequestManager,
    private  var  mListener:MoviesFragmentListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val MOVIE_ITEM:Int = 0;
        private const val LOADING:Int = 1;
        private const val EXHAUSTED:Int = 2;
    }


    @Volatile
    var movieList:ArrayList<Movie>? = ArrayList();

    interface MoviesFragmentListener{
        fun onMovieClick(id:String,position:Int)
        fun onDataRefresh()
        fun showProgress(flag:Boolean)
    }

    override fun getItemViewType(position: Int): Int {

        return when {
            movieList?.get(position)?.original_title.equals("LOADING...") -> {
                LOADING

            }
            movieList?.get(position)?.original_title.equals("EXHAUSTED...") -> {
                EXHAUSTED
            }
            else -> {
                MOVIE_ITEM
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
           LOADING ->{
                   return LoadingHolder(
                       itemView = LayoutInflater.from(parent.context).inflate(
                           R.layout.single_loading,
                           parent,
                           false
                       )
                   )

           }

            else -> {


                return MovieHolder(
                    mListener = mListener,
                    requestManager = requestManager,
                    itemView = LayoutInflater.from(parent.context).inflate(
                        R.layout.movies_view,
                        parent,
                        false
                    )
                )
            }

        }
    }

    override fun getItemCount(): Int {
        if(movieList==null){
            return 0
        }else{
            return movieList!!.size
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is MovieHolder ->{
                holder.bind(movie = movieList!!.get(position))
            }

            is LoadingHolder->{
                holder.bind()
            }
        }
    }


    fun setMovies(movies:ArrayList<Movie>?){
        movieList = movies
        notifyDataSetChanged()

        println("set movies has been called")
    }

    fun displayLoading(){

        if(movieList!!.size<Constants.PAGINATION_PAGE_SIZE){
            return
        }


        movieList!!.add( Movie(
            id=0,
            original_title = "LOADING...",
            overview = "",
            description = "",
            name = "",
            poster_path = "",
            director = "",
            runtime = 0,
            vote_average = 0.0,
            release_date = "",
            vote_count = "",
            tagline = "",
            backdrop_path = "",
            aID = 0
        ))
        notifyDataSetChanged()

        println("display loading  has been called")
    }

    fun removeLoading(){
        if(movieList!!.size>0 && movieList!!.get(movieList!!.size-1).original_title.equals("LOADING...")){
            movieList!!.removeAt(movieList!!.size - 1)
            notifyDataSetChanged()
        }
    }

    fun addAll(movies:ArrayList<Movie>?){
        movieList!!.addAll(movies!!);
        notifyDataSetChanged();
    }

    fun removeAll(movies:ArrayList<Movie>){
        movieList!!.removeAll(movies)
        notifyDataSetChanged()
    }

    fun remove(movie:Movie){
        movieList!!.remove(movie)
        notifyDataSetChanged()
    }

    fun remove(i:Int){
        movieList!!.removeAt(i);
        notifyDataSetChanged()
    }

    class LoadingHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        private  var p:ProgressBar? =null
        init {
            p = itemView.findViewById(R.id.progress)
        }

        fun bind(){

        }

    }

    class MovieHolder constructor(
        itemView : View,
        private var requestManager:RequestManager,
        private var mListener: MoviesFragmentListener

    ): RecyclerView.ViewHolder(itemView) {
        private var txtTitle:TextView?=null;
        private var txtReleaseDate:TextView?=null;
        private var txtVoteCount:TextView?=null;
        private var txtVote:TextView?=null;
        private var image:ImageView;
        private var ratingBar:RatingBar?=null;

        init{
            txtTitle = itemView.findViewById(R.id.title);
            txtReleaseDate=itemView.findViewById(R.id.release_date)
            txtVoteCount = itemView.findViewById(R.id.vote_count)
            txtVote = itemView.findViewById(R.id.vote)
            image = itemView.findViewById(R.id.imageView)
            ratingBar = itemView.findViewById(R.id.rating)
        }

        fun bind(movie:Movie){

            txtTitle?.text = movie.original_title
            txtVoteCount?.text = "Vote Count: ${movie.vote_count}"
            txtReleaseDate?.text = "Release Date: ${movie.release_date}"
            txtVote?.text = movie.vote_average.toString()

            requestManager
                .load(Constants.IMAGE_BASE_URL+movie.poster_path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)

            ratingBar?.max = 10;
            ratingBar?.rating = getVotePercentage(movie.vote_average.toFloat())

            itemView.setOnClickListener {
                mListener.onMovieClick(movie.id.toString(),adapterPosition)
            }
        }

        fun getVotePercentage(value:Float):Float{
            return  (value/10)*5
        }
    }
}