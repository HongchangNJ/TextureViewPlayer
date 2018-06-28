package com.han.videodemo

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Surface
import android.view.TextureView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.han.videodemo.utils.ScreenUtils
import com.han.videodemo.utils.UiUtils
import com.han.videodemo.utils.VideoSizeUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.lang.Exception

class MainActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    val mUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    val mLiveUrl = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    //val m233 = "rtmp://192.168.1.26/live/241"

    var mTextureView: TextureView? = null

    var mIjkPlayer: IjkMediaPlayer? = null
    var mPlayer: MediaPlayer? = null
    var mSurface: Surface? = null

    var mVideoWidth: Int? = 0
    var mVideoHeight: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        QMUIStatusBarHelper.translucent(this)
        initTextureView()
    }

    private fun initTextureView() {
        //mTextureView = TextureView(this)
        mTextureView = findViewById(R.id.textureview)
        mTextureView?.surfaceTextureListener = this
    }

    private fun play() {
        try {
            mPlayer = MediaPlayer()
            /*mPlayer?.setOnPreparedListener {player -> };
            mPlayer?.setOnCompletionListener { mediaPlayer ->  }
            mPlayer?.setOnBufferingUpdateListener{player, status ->}
            mPlayer?.setOnErrorListener(object : MediaPlayer.OnErrorListener {
                override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                    return true
                }
            })*/

            mPlayer?.setOnVideoSizeChangedListener(object : MediaPlayer.OnVideoSizeChangedListener {
                override fun onVideoSizeChanged(p0: MediaPlayer?, p1: Int, p2: Int) {
                    mVideoWidth = p0?.videoWidth
                    mVideoHeight = p0?.videoHeight

                    var screenWidth = ScreenUtils.getScreenWidth(this@MainActivity)
                    var textureViewHeight  = mVideoHeight?.times(screenWidth)?.div(mVideoWidth as Int)

                    mTextureView?.post({
                        var params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                textureViewHeight as Int)
                        params.addRule(RelativeLayout.CENTER_IN_PARENT)
                        mTextureView?.layoutParams = params
                        mTextureView?.requestLayout()
                    })
                }
            })
            mPlayer?.setDataSource(mUrl)
            mPlayer?.setSurface(mSurface)
            mPlayer?.setScreenOnWhilePlaying(true)
            mPlayer?.prepare()
            mPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playWithIjk() {
        try {
            mIjkPlayer = IjkMediaPlayer()
            /*mPlayer?.setOnPreparedListener {player -> };
            mPlayer?.setOnCompletionListener { mediaPlayer ->  }
            mPlayer?.setOnBufferingUpdateListener{player, status ->}
            mPlayer?.setOnErrorListener(object : MediaPlayer.OnErrorListener {
                override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                    return true
                }
            })*/

            mIjkPlayer?.setOnVideoSizeChangedListener({ iMediaPlayer: IMediaPlayer, i: Int, i1: Int, i2: Int, i3: Int ->
                mVideoWidth = iMediaPlayer?.videoWidth
                mVideoHeight = iMediaPlayer?.videoHeight

                var viewSize = VideoSizeUtils.caculateViewSize(this@MainActivity,
                        mVideoWidth as Int, mVideoHeight as Int)

                mTextureView?.post({
                    var params = RelativeLayout.LayoutParams(viewSize?.width as Int,
                            viewSize?.height)
                    params.addRule(RelativeLayout.CENTER_IN_PARENT)
                    mTextureView?.layoutParams = params
                    mTextureView?.requestLayout()
                })
            })

            mIjkPlayer?.setOnPreparedListener(object : IMediaPlayer.OnPreparedListener {
                override fun onPrepared(p0: IMediaPlayer?) {
                    mIjkPlayer?.start()
                }
            })
            mIjkPlayer?.setDataSource(mLiveUrl)
            mIjkPlayer?.setSurface(mSurface)
            mIjkPlayer?.setScreenOnWhilePlaying(true)
            mIjkPlayer?.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        try {
            mPlayer?.release()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return true
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        mSurface = Surface(p0)
        //play()
        playWithIjk()
    }
}
