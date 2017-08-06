package com.example.cadu.synesthesie;

import android.media.*;
import static android.os.SystemClock.elapsedRealtime;

public class GeradorAudio {

    public void rodar(int [] rgb){
        int sr=44100,bufferTam=AudioTrack.getMinBufferSize(sr,AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC,sr,AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT,bufferTam,AudioTrack.MODE_STREAM);
        short samples[] = new short[bufferTam];
        double f1 = 0.0,f2=0.0,f3=0.0;
        double f = FreqHSL(RGBtoHSL(rgb))*Math.pow(2, OitHSL(RGBtoHSL(rgb)));
        double amp = 10000;
        long t = elapsedRealtime();
        at.play();
        while((elapsedRealtime()-t)<=800) {
            for (int i = 0; i < bufferTam; i++) {
                samples[i] = (short) (amp*(Math.sin(f1)+Math.sin(f2)+Math.sin(f3))/3);
                f1 += f/sr;
                f2 += (f*5/4)/sr;
                f3 += (f*6/4)/sr;
            }

            at.write(samples,0,bufferTam);
        }
        at.stop();
        at.release();
    }

    private static double[] RGBtoHSL(int[] x){
        double r, g, b, cmax, cmin, delta, l, s, h;
        r = x[0]/255.0;
        g = x[1]/255.0;
        b = x[2]/255.0;
        cmax = Math.max(r, Math.max(g, b));
        cmin = Math.min(r, Math.min(g, b));
        delta = cmax - cmin;

        l = (cmax + cmin)/2;

        if(delta == 0) s = 0;
        else s = delta/(1-Math.abs(2*l - 1));

        if(delta == 0) h = 0;
        else if (cmax == r) h = (((g-b)/delta)%6)*60;
        else if (cmax == g) h = (((b-r)/delta)+2)*60;
        else h = (((r-g)/delta)+4)*60;
        if(h<0) h += 360;
        double[] retorno = {h, s, l};
        return retorno;
    }

    private static double OitHSL(double[] hsl){
        return 2*hsl[2];
    }

    private static double FreqHSL(double[] hsl){
        double h,f;
        h = hsl[0];
        f =  h*130.82*4/360+130.81*4;
        return f;
    }

}
