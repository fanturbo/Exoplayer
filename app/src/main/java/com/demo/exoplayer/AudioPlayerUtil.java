package com.demo.exoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;


import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

/**
 * 音频播放器
 */
public class AudioPlayerUtil {

    /**
     * 共通音频播放器实例
     */
    private static AudioPlayerUtil sAudioPlayer;
    private final Context mContext;
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    private SimpleExoPlayer simpleExoPlayer;

    private AudioPlayerUtil(Context appContext) {
        mContext = appContext;
        //step2. 创建播放器
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
    }

    /**
     * 取得音频播放器实例
     *
     * @param context context
     * @return 音频播放器实例
     */
    public static AudioPlayerUtil getInstance(Context context) {
        if (sAudioPlayer == null) {
            sAudioPlayer = new AudioPlayerUtil(context.getApplicationContext());
        }
        return sAudioPlayer;
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return simpleExoPlayer;
    }

    public void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer) {
        this.simpleExoPlayer = simpleExoPlayer;
    }

    /**
     * 停止播放，振动
     */
    public void stop() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * 判断是否在播放
     */
    public boolean isPlaying() {
        if (simpleExoPlayer != null) {
            return simpleExoPlayer.getPlayWhenReady();
        }
        return false;
    }

    public void seekTo(long position) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.seekTo(position);
        }
    }

    public void play(String url) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "语文同步学"), bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null);
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    public void setPlayRate(float speed) {
        PlaybackParameters playbackParameters = new PlaybackParameters(speed, 1.0F);
        simpleExoPlayer.setPlaybackParameters(playbackParameters);
    }

    /**
     * 获取音频时长
     *
     * @param url
     */
    public int getMediaDurtion(String url) {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(url);  //recordingFilePath（）为音频文件的路径
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int duration = player.getDuration();//获取音频的时间
        Log.d("ACETEST", "### duration: " + duration);
        player.release();//记得释放资源
        return duration;
    }
}

