package code;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.enums.X264_PROFILE;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;

import java.io.File;


public class videoEncoder {
    File source1 = null;
    File target = new File("target.mp4");
    vidInfo info1;
    double maxSize;
    public videoEncoder(File source) {
        source1 = source;
        info1 = new vidInfo(source1);
        /* Step 1. Declaring source file and Target file */
        /* Step 5. Do the Encoding*/

    }
    public void encodeToSize(double x, Float y, Float z){
        maxSize = x;
        if(z==0){
            z= Float.valueOf(info1.getDuration());
        }
        myEncode(y,z);
    }
    public static EncodingAttributes attributesMod(vidInfo sourceInfo, double ratio, float startTime, float Length){
        int modWidth = sourceInfo.getWidth();
        int modHeight = sourceInfo.getHeight();
        int modRate = (int)(sourceInfo.getBitRate()*ratio);
        EncodingAttributes attrs = null;
        if(modRate<300000) {
            attrs = attributer(32000, 1, 44100, modRate, 12, modWidth/4, modHeight/4, Length, startTime);
        }
        else if(modRate<480000) {
            attrs = attributer(32000, 1, 44100, modRate, 15, modWidth/2, modHeight/2, Length, startTime);
        }
        else if(modRate<1300000) {
            attrs = attributer(32000, 1, 44100, modRate, 24, (modWidth*2)/3, (modHeight*2)/3, Length, startTime);
        }
        if(modRate>1300000) {
            attrs = attributer(32000, 1, 44100, modRate, 30, modWidth, modHeight, Length, startTime);
        }
        return attrs;
    }
    public void myEncode(Float startTime,Float Length){
        vidInfo sourceInfo = new vidInfo(source1);
        Encoder encoder = new Encoder();
        double fileLength= source1.length();
        double sourceBytesPerSecond = fileLength/(sourceInfo.duration/1000);
        System.out.println("Bytes per second: "+sourceBytesPerSecond);
        int audiosize = (int) ((32*(sourceInfo.duration-startTime*1000))/8);
        if(audiosize>maxSize){
            audiosize = (int) ((16*(sourceInfo.duration-startTime*1000))/8);
        }
        double ratio = (maxSize-audiosize)/(fileLength-sourceBytesPerSecond*startTime);
        System.out.println("ratio 8MB/filesize: "+ratio);
        try {
            encoder.encode(new MultimediaObject(source1), target, attributesMod(sourceInfo,ratio,startTime,Length));
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        System.out.println("targetinfo");
        vidInfo targetInfo = new vidInfo(target);
    }
    public long sizeTest(){
        Encoder encoder = new Encoder();
        EncodingAttributes attrs = attributer(32000,1,44100,null,30,1280,720, Float.valueOf(30), null);
        try {
            encoder.encode(new MultimediaObject(source1), target, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target.length();
    }
    public static EncodingAttributes attributer(int AudioBitrate, int AudioChannels, int AudioSamplingrate, Integer VideoBitrate, int FrameRate, int Width, int Height, Float Seconds, Float startingPoint){
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
        audio.setBitRate(AudioBitrate);
        audio.setChannels(AudioChannels);
        audio.setSamplingRate(AudioSamplingrate);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setX264Profile(X264_PROFILE.BASELINE);
        video.setBitRate(VideoBitrate);
        video.setFrameRate(FrameRate);
        video.setSize(new VideoSize(Width, Height));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        attrs.setDuration(Seconds);
        attrs.setOffset(startingPoint);
        return attrs;
    }
    public static class vidInfo{
        int width = 0;
        int height = 0;
        int bitRate = 0;
        int duration = 0;
        public vidInfo(File source){
            MultimediaObject test = new MultimediaObject(source);
            MultimediaInfo info = null;
            String infoString = null;
            try {
                info = test.getInfo();
                infoString = info.toString();
            } catch (EncoderException e) {
                e.printStackTrace();
            }
            System.out.println(infoString);
            String[] splitted = infoString.split("[ ,]");
            duration = Integer.valueOf(splitted[3].replaceAll("[^\\d.]",""));
            width = Integer.valueOf(splitted[13].replaceAll("[^\\d.]",""));
            height = Integer.valueOf(splitted[15].replaceAll("[^\\d.]",""));
            bitRate = Integer.valueOf(splitted[17].replaceAll("[^\\d.]",""));
        }
        public int getWidth(){
            return width;
        }
        public int getHeight(){
            return height;
        }
        public int getBitRate(){
            return bitRate;
        }
        public int getDuration(){
            return duration;
        }
    }
}