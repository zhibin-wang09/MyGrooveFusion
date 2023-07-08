package beatmaker;
import com.google.common.io.ByteStreams;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * A class to manipulate the UI for the beat maker application
*/
public class UI {
    private final JFrame frame; // the base frame that the program uses
    private final BeatPlayer beatPlayer; // the music player that the program will be interacting with
    private final Container rootPane;
    private JPanel base;

    private final String APIEndpoint = "http://localhost:8080/api/v1/mixed_audios";
    
    /**
     * This constructor sets the default setting of the <code>frame</code>
    */
    public UI(){
        frame = new JFrame("Groove Fusion");
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
        base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));

        JButton info = new JButton( new ImageIcon("./app/src/main/resources/images/information.png"));
        info.addActionListener(e -> showGuide());
        base.add(info);
        base.add(new JPanel()); // place holder

        /* Add the libraries to menu bar then upon click on a specific library display library panel */
        frame.setJMenuBar(initMenu());

        JPanel product = new JPanel();
        JLabel productName = new JLabel("Production Beat");
        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> BeatPlayer.clear());


        JButton done = new JButton("Done");
        done.addActionListener(e -> produce());

        JButton share = new JButton("Share");
        share.addActionListener(e -> share());

        product.add(clear);
        product.add(done);
        product.add(share);
        product.add(productName);
        base.add(product);
        rootPane.add(base);
    }

    /**
     * Action to be implemented by the menu selection
     */
    private class MenuAction implements ActionListener{
        private final JPanel pane;
        public MenuAction(JPanel pane){
            this.pane = pane;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            changeLibraryPanel(pane);
        }
        
    }

    /**
     * Action lister that changes the content displayed on the UI
     * @param pane: The panel that contains list of audios to be displayed upon user selecting a specific audio library
     */
    private void changeLibraryPanel(JPanel pane){
        Component content = base.getComponent(1);
        if(content instanceof Library){
            beatPlayer.cleanUp((Library)content);
        }
        base.remove(1); // remove the content page
        base.add(pane, 1); // Update the content pane with the library pane
        base.validate();
        base.repaint();
    }

    /**
     * This function transfers all the libraries that exists into menu items.
     * Each item in the menu upon clicked will change the content the UI to the
     * audios in that specific library.
     * 
     * @return the menu bar component
     */
    public JMenuBar initMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu librarySelection = new JMenu("Library");
        for(Library lib : beatPlayer.getLibraries()){
            JMenuItem item = new JMenuItem(lib.getName());
            item.addActionListener(new MenuAction(lib));
            librarySelection.add(item);
        }
        menuBar.add(librarySelection);
        return menuBar;
    }

    /**
     * This function will prompt the user to choose the name of the production .wav file name and save location
     */
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

    public void share(){
        JFileChooser fileChooser = new JFileChooser("./app/src/main/resources/production");
        fileChooser.setFileFilter(new FileNameExtensionFilter("WAVE FILES","wav"));
        int response = fileChooser.showOpenDialog(null);
        if(response == JFileChooser.APPROVE_OPTION){
            File selected = fileChooser.getSelectedFile();
            String filename = selected.getName();
            int dotIndex = filename.lastIndexOf('.');
            if(!filename.substring(dotIndex+1).equals(".wav")){
                JOptionPane.showMessageDialog(null, "The file should be an .wav file!");
            }
            try(InputStream stream = new FileInputStream(selected)){
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
                byte[] buffer = new byte[4096];
                int byteRead;
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                while((byteRead = audioStream.read(buffer)) != -1){
                    byteStream.write(buffer,0,byteRead);
                }
                Service.sendData(APIEndpoint, byteStream.toByteArray());
            }catch(FileNotFoundException e){
                JOptionPane.showMessageDialog(null, "The file is not found!");
            }catch(IOException ioE){
                JOptionPane.showMessageDialog(null, "Can not read the content of the file!");
            }catch(UnsupportedAudioFileException unsupportedE){
                JOptionPane.showMessageDialog(null, "Not a valid audio file!");
            }
        }
    }

    /**
     * This function will display a guide upon opening the program
     */
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
