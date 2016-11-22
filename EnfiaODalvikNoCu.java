package com.example;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.*;


public class EnfiaODalvikNoCu {
    public static void main() throws Exception{
        TriangleOscillator  sineOsc1 = new TriangleOscillator();
        TriangleOscillator sineOsc2 = new TriangleOscillator();
        TriangleOscillator sineOsc3 = new TriangleOscillator();
        LineOut lineOut = new LineOut();
        Synthesizer synth = JSyn.createSynthesizer();
        sineOsc1.output.connect(0, lineOut.input, 0);
        sineOsc1.output.connect(0, lineOut.input, 1);
        sineOsc2.output.connect(0, lineOut.input, 0);
        sineOsc2.output.connect(0, lineOut.input, 1);
        sineOsc3.output.connect(0, lineOut.input, 0);
        sineOsc3.output.connect(0, lineOut.input, 1);
        int[] rgb = {40,54,86};
        double f = FreqHSL(RGBtoHSL(rgb))*Math.pow(2, OitHSL(RGBtoHSL(rgb)));
        sineOsc1.frequency.set(f);
        sineOsc1.amplitude.set(0.05);
        sineOsc2.frequency.set(f/1.8);
        sineOsc2.amplitude.set(0.05);
        sineOsc3.frequency.set(f/1.5);
        sineOsc3.amplitude.set(0.05);
        synth.add(lineOut);
        synth.add(sineOsc1);
        synth.add(sineOsc2);
        synth.add(sineOsc3);
        synth.start(16000);
        lineOut.start();
        synth.sleepFor(2.0);
        lineOut.stop();
        synth.stop();
        
    }
    public static double[] RGBtoHSL(int[] x){
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
    
    public static double OitHSL(double[] hsl){
        return 5*hsl[2];
    }
    
    public static double FreqHSL(double[] hsl){
        double h, l, f;
        h = hsl[0];
        l = hsl[2];
        
        f =  h*130.82/360+130.81;
        return f;
    }
}
