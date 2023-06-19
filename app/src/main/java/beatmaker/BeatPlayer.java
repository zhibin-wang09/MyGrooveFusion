package beatmaker;
import java.util.ArrayList;

import javax.swing.JPanel;

import java.io.File;



/**
 * A class that stores all the audios and manipulate these audios
 */
public class BeatPlayer extends JPanel{

    private ArrayList<Audio> audios; // stores all the audios
    final File audioFolder = new File("./app/src/main/resources/audios"); // open the directory of audio files
    
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
    }

    /**
     * This function returns all the audios on record
     * @return a list of all the audios
     */
    public ArrayList<Audio> getAudios(){
        return this.audios;
    }
}
