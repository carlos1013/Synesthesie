package testes.cores;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.*;
import java.util.*;

public class AudioSynth01{

  AudioFormat audioFormat;
  AudioInputStream audioInputStream;
  SourceDataLine sourceDataLine;

  float sampleRate = 16000;
  int sampleSizeInBits = 16;
  //Allowable 8,16
  int channels = 1;
  //Allowable 1,2
  boolean signed = true;
  //Allowable true,false
  boolean bigEndian = true;
  //Allowable true,false

  //A buffer to hold two seconds monaural and one
  // second stereo data at 16000 samp/sec for
  // 16-bit samples
  byte audioData[] = new byte[16000*4];

  public static void main(String args[]){
    new AudioSynth01();
  }
  
  public AudioSynth01(){
    new SynGen().getSyntheticData(audioData);
    playOrFileData();

  }//end constructor
  //-------------------------------------------//

  //This method plays or files the synthetic
  // audio data that has been generated and saved
  // in an array in memory.
  private void playOrFileData() {
    try{
      //Get an input stream on the byte array
      // containing the data
      InputStream byteArrayInputStream =
                        new ByteArrayInputStream(
                                      audioData);

      //Get the required audio format
      audioFormat = new AudioFormat(
                                sampleRate,
                                sampleSizeInBits,
                                channels,
                                signed,
                                bigEndian);

      //Get an audio input stream from the
      // ByteArrayInputStream
      audioInputStream = new AudioInputStream(
                    byteArrayInputStream,
                    audioFormat,
                    audioData.length/audioFormat.
                                 getFrameSize());

      //Get info on the required data line
      DataLine.Info dataLineInfo =
                          new DataLine.Info(
                            SourceDataLine.class,
                                    audioFormat);

      //Get a SourceDataLine object
      sourceDataLine = (SourceDataLine)
                             AudioSystem.getLine(
                                   dataLineInfo);
      //Decide whether to play the synthetic
      // data immediately, or to write it into
      // an audio file, based on the user
      // selection of the radio buttons in the
      // South of the GUI..
      
      //Create a thread to play back the data and
      // start it running.  It will run until all
      // the data has been played back
      new ListenThread().start();
      
    }catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }//end catch
  }//end playOrFileData
//=============================================//

//Inner class to play back the data that was
// saved.
class ListenThread extends Thread{
  //This is a working buffer used to transfer
  // the data between the AudioInputStream and
  // the SourceDataLine.  The size is rather
  // arbitrary.
  byte playBuffer[] = new byte[16384];

  public void run(){
    try{
      //Open and start the SourceDataLine
      sourceDataLine.open(audioFormat);
      sourceDataLine.start();

      int cnt;
      //Get beginning of elapsed time for
      // playback
      long startTime = new Date().getTime();

      //Transfer the audio data to the speakers
      while((cnt = audioInputStream.read(
                              playBuffer, 0,
                              playBuffer.length))
                                          != -1){
        //Keep looping until the input read
        // method returns -1 for empty stream.
        if(cnt > 0){
          //Write data to the internal buffer of
          // the data line where it will be
          // delivered to the speakers in real
          // time
          sourceDataLine.write(
                             playBuffer, 0, cnt);
        }//end if
      }//end while

      //Block and wait for internal buffer of the
      // SourceDataLine to become empty.
      sourceDataLine.drain();


      //Get and display the elapsed time for
      // the previous playback.
      int elapsedTime =
         (int)(new Date().getTime() - startTime);
      System.out.println(elapsedTime);

      //Finish with the SourceDataLine
      sourceDataLine.stop();
      sourceDataLine.close();

    }catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }//end catch

  }//end run
}//end inner class ListenThread
//=============================================//

//Inner signal generator class.

//An object of this class can be used to
// generate a variety of different synthetic
// audio signals.  Each time the getSyntheticData
// method is called on an object of this class,
// the method will fill the incoming array with
// the samples for a synthetic signal.
class SynGen{
  //Note:  Because this class uses a ByteBuffer
  // asShortBuffer to handle the data, it can
  // only be used to generate signed 16-bit
  // data.
  ByteBuffer byteBuffer;
  ShortBuffer shortBuffer;
  int byteLength;

  void getSyntheticData(byte[] synDataBuffer){
    //Prepare the ByteBuffer and the shortBuffer
    // for use
    byteBuffer = ByteBuffer.wrap(synDataBuffer); 
    shortBuffer = byteBuffer.asShortBuffer();

    byteLength = synDataBuffer.length;

    tones();
  }//end getSyntheticData method
  //-------------------------------------------//

  //This method generates a monaural tone
  // consisting of the sum of three sinusoids.
  void tones(){
    channels = 1;//Java allows 1 or 2
    //Each channel requires two 8-bit bytes per
    // 16-bit sample.
    int bytesPerSamp = 2;
    sampleRate = 16000.0F;
    // Allowable 8000,11025,16000,22050,44100
    int sampLength = byteLength/bytesPerSamp;
    int[] rgb = {40,54,86};
    for(int cnt = 0; cnt < sampLength; cnt++){
      double time = cnt/sampleRate;
      double freq = FreqHSL(RGBtoHSL(rgb))*Math.pow(2, OitHSL(RGBtoHSL(rgb)));
      double sinValue =
        (Math.sin(2*Math.PI*freq*time) +
        Math.sin(2*Math.PI*(freq/1.8)*time) +
        Math.sin(2*Math.PI*(freq/1.5)*time))/3.0;
      shortBuffer.put((short)(16000*sinValue));
    }//end for loop
  }//end method tones
  //-------------------------------------------//
  
  public double[] RGBtoHSL(int[] x){
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
    
    public double OitHSL(double[] hsl){
        return 5*hsl[2];
    }
    
    public double FreqHSL(double[] hsl){
        double h, l, f;
        h = hsl[0];
        l = hsl[2];
        
        f =  h*130.82/360+130.81;
        return f;
    }

}//end SynGen class
//=============================================//

}//end outer class AudioSynth01.java