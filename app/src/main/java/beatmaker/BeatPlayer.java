package beatmaker;
import java.util.ArrayList;

import javax.swing.JPanel;

import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * A class that stores all the audios and manipulate these audios
 */
public class BeatPlayer extends JPanel{

    private ArrayList<Audio> audios; // stores all the audios
    final File audioFolder = new File("./app/src/main/resources/audios"); // open the directory of audio files
    public static ArrayList<AudioInputStream> clips; // the clips that are going to be concatenated.
    
    /**
     * Visit the directory that holds all the audio files then convert them into a 
     * Audio object and store them into a list.
     */
    public BeatPlayer(){
        audios = new ArrayList<>();
        for(final File audioFile : audioFolder.listFiles()){ // explore the directory then add all the audio files
            Audio audio = new Audio(audioFile.getName(),audioFile);
            audios.add(audio);
        }
        clips = new ArrayList<>();
    }

    /**
     * This function returns all the audios on record
     * @return a list of all the audios
     */
    public ArrayList<Audio> getAudios(){
        return this.audios;
    }

    /**
     * This function will create copy the audioFile[startframe, endFrame] and store it
     * in <code>clips</code> Then it will be concatenated after all the desired beats are added.
     * 
     * @param sourceAudioFile the audio to copy from  
     * @param format used to create the range clip with the original format
     * @param startFrame the start position
     * @param secondsToCopy the amount of time to transfer since <code>startFrame</code>
     */
    public static void copyAudio(String sourceFileName, long startFrame, long framesToCopy){
        AudioInputStream inputStream = null;
        AudioInputStream shortenedStream = null;
        try {
        File file = new File(sourceFileName);
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        AudioFormat format = fileFormat.getFormat();
        inputStream = AudioSystem.getAudioInputStream(file);
        inputStream.skip(startFrame);
        long framesOfAudioToCopy = framesToCopy;
        shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
        } catch (Exception e) {
            System.out.println(e);
        }
        if(inputStream != null) System.out.println("original length: " + (long)(inputStream.getFrameLength()));
        if(shortenedStream!=null) System.out.println("clip length: " + (long) shortenedStream.getFrameLength());
        clips.add(shortenedStream);

        System.out.println();
    }

    /**
     * This function suppress resouce because <code>clip</code> can not be closed due to closing a sequence input stream closes all its underlying streams, 
     * and closing audio input streams closes its underlying steam as well. 
     */
    @SuppressWarnings("resource")
    public static void joinClips(){
        AudioInputStream prev = null;
        for(AudioInputStream clip : clips){
            if(prev == null){
                prev = clip;
                continue;
            }
            AudioInputStream concat = new AudioInputStream(new SequenceInputStream(prev, clip),prev.getFormat(),prev.getFrameLength() + clip.getFrameLength());
            prev = concat;
            System.out.println(prev.getFrameLength());
        }
        try{
            AudioSystem.write(prev, AudioFileFormat.Type.WAVE, new File("production.wav"));
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void clear(){
        clips = new ArrayList<>();
    }
}
