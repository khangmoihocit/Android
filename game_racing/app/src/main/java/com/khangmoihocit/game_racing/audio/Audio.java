package com.khangmoihocit.game_racing.audio;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.khangmoihocit.game_racing.R;

public class Audio {

    private SoundPool sp;
    private int sCoin = 0, sHit = 0, sNitro = 0; // [cite: 121]
    private boolean ready = false; // [cite: 121]

    public void init(Context c) { // [cite: 122]
        try {
            // [cite: 122, 123]
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            // [cite: 123]
            sp = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .setAudioAttributes(aa)
                    .build();

            // Load âm thanh từ res/raw
            sCoin = sp.load(c, R.raw.pickup_coin, 1); // [cite: 123]
            sHit = sp.load(c, R.raw.hit, 1); // [cite: 123]
            sNitro = sp.load(c, R.raw.nitro, 1); // [cite: 123]
            ready = true;
        } catch (Exception ignore) {
            ready = false;
        }
    }

    public void release() { // [cite: 124]
        if (sp != null) sp.release();
        sp = null;
        ready = false;
    }

    // Các hàm tiện ích để chơi âm thanh
    public void coin() { play(sCoin); } // [cite: 124, 125]
    public void hit() { play(sHit); } // [cite: 125]
    public void nitro() { play(sNitro); } // [cite: 125]

    private void play(int id) { // [cite: 128]
        if (ready && sp != null) {
            sp.play(id, 1, 1, 0, 0, 1);
        }
    }
}