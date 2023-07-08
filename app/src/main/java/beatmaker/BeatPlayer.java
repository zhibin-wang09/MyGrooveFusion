package beatmaker;
import java.util.*;

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

    private final HashMap<String, Library> libraries; // stores all the libraries
    final File libraryFolder = new File("./app/src/main/resources/audios"); // open the directory of audio files
    public static ArrayList<AudioInputStream> clips; // the clips that are going to be concatenated.
    /* Perhaps that inside the clips array, I can have a list of audios but each one tagged with a different purpose
     * for instance, [audio_1, audio_2,audio3,audio_4] audio_1 and audio_2 are tagged with concat meaning audio_2 is to concat
     * with audio_1. But audio_3 is tagged merge, that means audio_3 is going to be merged with the result of audio_1 and audio_2. Lastly,
     * audio_4 is tagged merge as well, then it will merge with the result of all previous audios.
     */
    
    /**
     * Visit the directory that holds all the audio files then convert them into a 
     * Audio object and store them into a list.
     */
    public BeatPlayer(){
        libraries = new HashMap<>();
        for(final File library : Objects.requireNonNull(libraryFolder.listFiles())){ // explore the directory then add all the audio files in every library
            if(library.getName().equals(".DS_Store")) continue;
            Library lib = new Library(library.getName());
            for(final File audioFile : library.listFiles()){
                lib.addAudio(new Audio(audioFile.getName(),audioFile));
            }
            lib.createPanel();
            libraries.put(library.getName(), lib);
        }
        clips = new ArrayList<>();
    }

    /**
     * This function will create copy the audioFile[startFrame, endFrame] and store it
     * in <code>clips</code> Then it will be concatenated after all the desired beats are added.
     * 
     * @param sourceFileName the audio to copy from
     * @param startFrame the start position
     * @param framesToCopy the amount of time to transfer since <code>startFrame</code>
     */
    public static void copyAudio(String sourceFileName, long startFrame, long framesToCopy){
        AudioInputStream inputStream = null; // the source stream
        AudioInputStream shortenedStream = null; // the small chunk of beat
        try {
            /* open file instead of reusing audioInputStream from Audio because they have different pointers. */
            File file = new File(sourceFileName);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat format = fileFormat.getFormat();

            /* get another instance of AudioInputStream */
            inputStream = AudioSystem.getAudioInputStream(file);
            inputStream.skip(startFrame); // skip to the startFrame byte
            shortenedStream = new AudioInputStream(inputStream, format, framesToCopy); // new stream that has the small chunk of beat
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        if(inputStream != null) System.out.println("original length: " + (long)(inputStream.getFrameLength()));
        if(shortenedStream!=null) System.out.println("clip length: " + (long) shortenedStream.getFrameLength());
        clips.add(shortenedStream); // add into the list of beats that are going to be concatenated
    }

    /**
     * This function suppress resource because <code>clip</code> can not be closed due to closing a sequence input stream closes all its underlying streams,
     * and closing audio input streams closes its underlying steam as well. This function goes over all the added clips and concatenate these clips
     * into a new beat then convert it to a new file stored in resources/production.
     * 
     * @param destination: the calling method need to specify where to store this file.
     * @return the status of the function
     */
    @SuppressWarnings("resource")
    public static boolean joinClips(String destination){
        AudioInputStream prev = null; // previous beat

        /* for every beat in clips concatenate them */
        for(AudioInputStream clip : clips){
            if(prev == null){
                prev = clip;
                continue;
            }
            prev = new AudioInputStream(new SequenceInputStream(prev, clip),prev.getFormat(),prev.getFrameLength() + clip.getFrameLength());
        }
        if(prev == null) return false;
        try{
            AudioSystem.write(prev, AudioFileFormat.Type.WAVE, new File(destination)); // create new file of the final product beat
            return true;
        }catch(IOException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public static void clear(){
        clips = new ArrayList<>();
    }

    public ArrayList<Library> getLibraries(){
        ArrayList<Library> listOfLib = new ArrayList<>();
        for(Map.Entry<String, Library> e : libraries.entrySet()){
            listOfLib.add(e.getValue());
        }
        return listOfLib;
    }

    public void cleanUp(Library lib){
        lib.closeAll();
    }
}
