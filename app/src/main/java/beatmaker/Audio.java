package beatmaker;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

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
        JButton play = new JButton("PLAY", null);
        play.addActionListener(e -> play());
        JButton stop = new JButton("STOP", null);
        stop.addActionListener(e -> stop());
        JButton resume = new JButton("RESUME", null);
        resume.addActionListener(e-> resume());
        JButton add = new JButton("ADD", null);
        JLabel label = new JLabel(getName());
        slider = new JSlider(0, frame,0);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
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
     * This method will initialize the clip of audio if it hasn't already and play. If the
     * audio is already playing and upon activation of method again it will restart the audio.
     */
    public void play(){
        if(clip == null) init();
        clip.setMicrosecondPosition(0);
        clip.start();
        while(clip.isRunning()){
            slider.setValue(clip.getFramePosition());
        }
    }

    /**
     * This method simply re-start the audio from where it left off.
     */
    public void resume(){
        if(clip == null) init();
        clip.start();
    }

    /**
     * This method simply stops the audio.
     */
    public void stop(){
        if(clip == null) init();
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
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
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
