package com.han.videodemo.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.han.videodemo.utils.UiUtils
import com.han.videodemo.utils.VideoSizeUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * created by HanHongchang
 * 2018/6/28
 *
 */
class DemandActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {


    lateinit var mTextureView: TextureView
    lateinit var mMediaPlayer: MediaPlayer
    val mUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var container = RelativeLayout(this)
        var containerParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        container.layoutParams = containerParams
        container.setBackgroundColor(Color.BLACK)


        mTextureView = TextureView(this)
        var layoutParams = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                UiUtils.dip2Px(this, 200))
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        mTextureView?.layoutParams = layoutParams
        mTextureView.surfaceTextureListener = this

        container.addView(mTextureView)

        setContentView(container)
        QMUIStatusBarHelper.translucent(this)
    }

    private fun startPlay(surface: Surface) {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setDataSource(mUrl)
        mMediaPlayer.setSurface(surface)
        mMediaPlayer.setOnVideoSizeChangedListener({ mediaPlayer: MediaPlayer, width: Int, height: Int ->
            var viewSize = VideoSizeUtils.caculateViewSize(this@DemandActivity, width, height)
            var params = mTextureView.layoutParams
            params.width = viewSize.width
            params.height = viewSize.height
        })
        mMediaPlayer.setScreenOnWhilePlaying(true)

        mMediaPlayer.setOnPreparedListener({mp: MediaPlayer? ->  mMediaPlayer.start()})
        mMediaPlayer.prepareAsync()
    }




    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        return true
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        var surface = Surface(p0)
        startPlay(surface)
    }

    override fun onPause() {
        super.onPause()
        if (mMediaPlayer != null && mMediaPlayer.isPlaying) {
            //mMediaPlayer.pause()
            mMediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMediaPlayer != null) {
            mMediaPlayer.release()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            var intent = Intent(context, DemandActivity::class.java)
            context.startActivity(intent)
        }
    }
}