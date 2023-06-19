package beatmaker;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Container;

import java.util.ArrayList;

/**
 * A class to manipulate the UI for the beat maker application
 */
public class UI {
    JFrame frame; // the base frame that the program uses
    BeatPlayer beatPlayer; // the music player that the program will be interacting with

    /**
     * This constructor sets the default setting of the <code>frame</code>
    */
    public UI(){
        frame = new JFrame("Beat Maker");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // change default behavior to exit on close
        frame.setResizable(false);
        frame.setSize(900,600);
        frame.setIconImage(new ImageIcon("../resources/images/beats.png").getImage());
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        beatPlayer = new BeatPlayer();
    }

    /**
     * This method will display the frame to the user
    */
    public void show(){
        ArrayList<JPanel> panels = generatePanels();
        Container rootPane = frame.getContentPane(); 
        for(JPanel p : panels){
            rootPane.add(p);
        }
        frame.setVisible(true); // JFrame is initially invisible, so set visible
    }

    /**
     * This function generate the list of audio players for each audio that 
     * exist in <code>beatPlayer</code>'s list of audios.
     * 
     * @return a list of JPanels that can be implemented to <code>frame</code>
     */
    private ArrayList<JPanel> generatePanels(){
        ArrayList<JPanel> panels = new ArrayList<>();
        for(Audio audio : beatPlayer.getAudios()){
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            JButton play = new JButton("PLAY", null);
            JButton stop = new JButton("STOP", null);
            JButton resume = new JButton("RESUME", null);
            JLabel label = new JLabel(audio.getName() );
            panel.add(play);
            panel.add(stop);
            panel.add(resume);
            panel.add(label);
            panels.add(panel);
        }
        return panels;
    }
}
