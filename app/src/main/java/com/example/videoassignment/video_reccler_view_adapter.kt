package com.example.videoassignment

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView

class video_reccler_view_adapter(private val context: Context) : RecyclerView.Adapter<video_reccler_view_adapter.videoViewHolder>(){
    private val videoList: MutableList<video_data_class> = mutableListOf()
    private var player: ExoPlayer? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): video_reccler_view_adapter.videoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_recycler_view_item, parent, false)
        return videoViewHolder(view)
    }
    override fun onBindViewHolder(holder: videoViewHolder, position: Int) {
        val currentVideo = videoList[position]
        holder.bindVideo(currentVideo)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
    inner class videoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playerView: PlayerView = itemView.findViewById(R.id.video_view)
        private val title: TextView = itemView.findViewById(R.id.text_view_title)
        private val description: TextView = itemView.findViewById(R.id.text_view_description)
        private val channel: TextView = itemView.findViewById(R.id.text_view_channel)
        private val likes: TextView = itemView.findViewById(R.id.text_view_like)

        private var player: ExoPlayer? = null

        fun bindVideo(video: video_data_class) {
            player = ExoPlayer.Builder(context).build()
            playerView.player = player
            val mediaItem = MediaItem.fromUri(Uri.parse(video.Video_URL.toString()))
            player?.setMediaItem(mediaItem)
            player?.prepare()

            title.text = video.Title
            description.text = video.Description
            channel.text = video.Channel_Name
            likes.text = video.Likes.toString()
        }

        fun releasePlayer() {
            player?.release()
            player = null
        }
    }

override fun onViewRecycled(holder: videoViewHolder) {
    super.onViewRecycled(holder)
    holder.releasePlayer()
}
    fun setData(videos: List<video_data_class>) {
        videoList.clear()
        videoList.addAll(videos)
        notifyDataSetChanged()
    }
}