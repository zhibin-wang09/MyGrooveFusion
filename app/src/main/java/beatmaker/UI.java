package beatmaker;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Component;
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
        frame.setIconImage(new ImageIcon("./app/src/main/resources/images/beats.png").getImage());
        rootPane = frame.getContentPane();
        beatPlayer = new BeatPlayer();
    }

    /**
     * This method will display the frame to the user
    */
    public void show(){
        addPanels();
        frame.setVisible(true); // JFrame is initially invisible, so set visible
        showGuide();
    }

    /**
     * This function generate the list of panels for each audio that 
     * exist in <code>beatPlayer</code>'s list of audios. Use the list to
     * add the panels into the frame.
    */
    private void addPanels(){
        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));

        JPanel libraryBase = new JPanel();
        for(Audio audio : beatPlayer.getAudios()){ // for every audio that exist create a panel
            libraryBase.add(audio); // root pane add instead of JFrame
            audio.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        libraryBase.setLayout(new BoxLayout(libraryBase, BoxLayout.Y_AXIS)); // the libraryBase will display the panels
        JScrollPane scroller = new JScrollPane(libraryBase,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // add a scroll bar to the base
        base.add(scroller);

        JPanel product = new JPanel();
        JLabel productName = new JLabel("Result Beat");
        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> BeatPlayer.clear());

        JButton upload = new JButton("Upload");


        JButton done = new JButton("Done");
        done.addActionListener(e -> produce());

        product.add(clear);
        product.add(upload);
        product.add(done);
        product.add(productName);
        base.add(product);
        rootPane.add(base);
    }

    public void produce(){
        JFileChooser fileChooser = new JFileChooser("./app/src/main/resources/production");
        fileChooser.setFileFilter(new FileNameExtensionFilter("WAVE FILES", "wav", "wave"));
        int response = fileChooser.showSaveDialog(null);
        if(response == JFileChooser.APPROVE_OPTION){
            boolean status = BeatPlayer.joinClips(fileChooser.getSelectedFile().getAbsolutePath());
            if(status){
                JOptionPane.showMessageDialog(null,"The file has been added successfully!");
            }
        }
    }

    public void showGuide(){
        /* <a href="https://www.flaticon.com/free-icons/midi" title="midi icons">Midi icons created by Freepik - Flaticon</a> */
        JOptionPane.showMessageDialog(null, 
         """
            This is a brief guide on how to use this program.\n
            \t1. The left most button is the play button. Pressing the button will always starts at the beginning of the audio.\n
            \t2. The second button to left is the pause button, it pauses the audio.\n
            \t3. Then it's the resume button, it will start where it left off.\n
            \t4. Lastly, it's the add button which clips piece of the audio and uses it to make new beats.\n
            \t5. The slider is used to precisely choose where you want the beat to start. It can also be used to choose the starting point of the clip.\n
            \t6. The \"clip\" means the range of audio/beat that is going to be extracted and added to the result track.\n
            \tThis \"clip\" starts at the slider position after you moved it and stops when you hit stop and add.\n
            \t7. The small panel in the bottom is used to control the intermediate steps of the final audio/beat build.\n
            \t\ta. The first button clears the \"clips\" that have been added\n
            \t\tb. The upload button uploads the button to a website (You can configure this website by heading to the web folder).\n
            \t\tc. The finish button builds the final production audio/beat. you will be prompt a window to locate where to store the production.\n
            You can always come back to this panel for information. \n.
            HAVE FUN MESSING AROUND!.\n
         """, "Brief Guide" ,JOptionPane.INFORMATION_MESSAGE, new ImageIcon("./app/src/main/resources/images/midi.png"));
    }
}
