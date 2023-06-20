package beatmaker;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.Container;

/**
 * A class to manipulate the UI for the beat maker application
*/
public class UI {
    private JFrame frame; // the base frame that the program uses
    private BeatPlayer beatPlayer; // the music player that the program will be interacting with
    private Container rootPane;

    
    /**
     * This constructor sets the default setting of the <code>frame</code>
    */
    public UI(){
        frame = new JFrame("Beat Maker");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // change default behavior to exit on close
        frame.setResizable(false);
        frame.setSize(900,600);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("../resources/images/beats.png").getImage());
        rootPane = frame.getContentPane();
        beatPlayer = new BeatPlayer();
    }

    /**
     * This method will display the frame to the user
    */
    public void show(){
        addPanels();
        frame.setVisible(true); // JFrame is initially invisible, so set visible
    }

    /**
     * This function generate the list of panels for each audio that 
     * exist in <code>beatPlayer</code>'s list of audios. Use the list to
     * add the panels into the frame.
    */
    private void addPanels(){
        JPanel base = new JPanel();
        for(Audio audio : beatPlayer.getAudios()){ // for every audio that exist create a panel
            base.add(audio); // root pane add instead of JFrame
        }

        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS)); // the base will display the panels
        JScrollPane scroller = new JScrollPane(base,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // add a scroll bar to the base
        rootPane.add(scroller);
    }
}
