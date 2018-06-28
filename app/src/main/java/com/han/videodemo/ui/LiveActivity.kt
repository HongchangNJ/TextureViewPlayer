package com.han.videodemo.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Surface
import android.view.TextureView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.han.videodemo.utils.UiUtils
import com.han.videodemo.utils.VideoSizeUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * created by HanHongchang
 * 2018/6/28
 *
 */
class LiveActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {
    lateinit var mTextureView: TextureView
    lateinit var mMediaPlayer: IjkMediaPlayer
    val mLiveUrl = "rtmp://live.hkstv.hk.lxdns.com/live/hks";



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
        mMediaPlayer = IjkMediaPlayer()
        mMediaPlayer.setDataSource(mLiveUrl)
        mMediaPlayer.setSurface(surface)
        mMediaPlayer.setOnVideoSizeChangedListener({ iMediaPlayer: IMediaPlayer, i: Int, i1: Int, i2: Int, i3: Int ->
            var viewSize = VideoSizeUtils.caculateViewSize(this@LiveActivity,
                    iMediaPlayer.videoWidth, iMediaPlayer.videoHeight)
            var params = mTextureView.layoutParams
            params.width = viewSize.width
            params.height = viewSize.height
        })
        mMediaPlayer.setScreenOnWhilePlaying(true)

        mMediaPlayer.setOnPreparedListener({
            mMediaPlayer.start()
        })
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
            var intent = Intent(context, LiveActivity::class.java)
            context.startActivity(intent)
        }
    }
}