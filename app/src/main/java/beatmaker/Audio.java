package beatmaker;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;

/**
 * A class that holds data for a audio file and provides
 * methods to interact with these audio files.
 */
public class Audio extends JPanel{
    //private byte[] data;
    private String fileName;
    private File audioFile;
    private Clip clip;
    private int frame = 0;
    private JSlider slider = null;
    private AudioInputStream audioStream;

    public Audio(String fileName, File audioFile) {
        this.fileName = fileName;
        this.audioFile = audioFile;
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        addButtons();
    }

    /**
     * This method uses the base that an Audio is a JPanel so it adds 
     * the buttons to itself. In addition to the action listeners that
     * these buttons corresponds to.
    */
    private void addButtons(){
        /* <a href="https://www.flaticon.com/free-icons/play-button" title="play button icons">Play button icons created by Freepik - Flaticon</a> */
        JButton play = new JButton(new ImageIcon("./app/src/main/resources/images/play-button.png"));
        play.setBorder(null);
        play.setMargin(new Insets(10, 20, 10, 20));
        play.addActionListener(e -> play());

        /* <a href="https://www.flaticon.com/free-icons/pause-button" title="pause button icons">Pause button icons created by Icon Hubs - Flaticon</a> */
        JButton stop = new JButton(new ImageIcon("./app/src/main/resources/images/pause-button.png"));
        stop.setBorder(null);
        stop.setMargin(new Insets(10, 20, 10, 20));
        stop.addActionListener(e -> stop());

        /* <a href="https://www.flaticon.com/free-icons/end" title="end icons">End icons created by Icon.doit - Flaticon</a> */
        JButton resume = new JButton(new ImageIcon("./app/src/main/resources/images/resume-button.png"));
        resume.setBorder(null);
        stop.setMargin(new Insets(10, 20, 10, 20));
        resume.addActionListener(e-> resume());

        /* <a href="https://www.flaticon.com/free-icons/more" title="more icons">More icons created by Pixel perfect - Flaticon</a> */
        JButton add = new JButton(new ImageIcon("./app/src/main/resources/images/add-button.png"));
        add.setBorder(null);
        add.setMargin(new Insets(10, 20, 10, 20));
        add.addActionListener(e -> transferRange());

        JLabel label = new JLabel(getName());
        slider = new JSlider(0, frame,0);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(clip == null) return;
                clip.setFramePosition(slider.getValue());
            }
            
        }); // this method will move the position in the the audio by the slider value
        this.add(play);
        this.add(stop);
        this.add(resume);
        this.add(add);
        this.add(label);
        this.add(slider);
    }

    /**
     * This method is activated upon user clicking the ADD button to set a range
     * then transfer this range of bytes to the output audio.
     */
    private void transferRange(){
        if(clip == null || !clip.isOpen()) return;

        BeatPlayer.copyAudio(audioFile.getPath(),(long)slider.getValue(), clip.getLongFramePosition() - slider.getValue());
    }

    /**
     * This method will initialize the clip of audio if it hasn't already and play. If the
     * audio is already playing and upon activation of method again it will restart the audio.
     */
    public void play(){
        if(clip == null || !clip.isOpen()) init();
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    /**
     * This method simply re-start the audio from where it left off.
     */
    public void resume(){
        if(clip == null || !clip.isOpen()) init();
        clip.start();
    }

    /**
     * This method simply stops the audio.
     */
    public void stop(){
        if(clip == null || !clip.isOpen()) init();
        clip.stop();
    }

    /**
     * This is a private method that is used by clip manipulation methods such as play, resume, and stop
     * to initialize the clip if they haven't already. The reason for this method not be included in the 
     * constructor is to safe memory as when the clip opens it is loaded into memory. So if it is not needed
     * then it should not be initialized.
     */
    private void init(){
        try{
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            this.clip = AudioSystem.getClip();
            clip.open(audioStream);
            slider.setMaximum(clip.getFrameLength());
        }catch(UnsupportedAudioFileException audioE){
            System.out.println("Audio Not Supported!");
        }catch(IOException ioE){
            System.out.println("IOException!");
        }catch(LineUnavailableException lineE){
            System.out.println("Line Not Available!");
        }
    }

    /**
     * This is a getter that gives the name of the audio file.
     * @return the filename of the audio.
     */
    public String getName(){
        return fileName;
    }
}
