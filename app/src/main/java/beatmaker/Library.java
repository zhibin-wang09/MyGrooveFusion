package beatmaker;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This is the class that keeps all the audio of a related category together.
 * This is a JPanel that can be selected by the base UI to display all audios
 * inside this library.
 */
public class Library extends JPanel{
    private final ArrayList<Audio> audios; // library holds audios of similar category
    private final String name;

    public Library(String name){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  // a box layout is used for the audios 
        new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // add a scroll bar to the library display
        audios  = new ArrayList<>();
        this.name = name;
    }

    public void createPanel(){
        for(Audio a : audios){
            this.add(a);
        }
    }

    /**
     * This function adds a audio to the library
     * @param audio: the audio to be added
     */
    public void addAudio(Audio audio){
        audios.add(audio);
    }

    /**
     * This function returns all the audios that the library currently have
     * @return a list of audios
     */
    public ArrayList<Audio> getAudios(){
        return this.audios;
    }

    /**
     * Getter for the name of this library
     * @return name of the library
     */
    public String getName(){
        return this.name;
    }
}
