package beatmaker;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class UI {
    JFrame frame;
    public UI(){
        frame = new JFrame("Beat Maker");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // makes the default action of closing frame is to do nothing
    }

    public void show(){
        
        frame.setVisible(true); // JFrame is initially invisible, so set visible
    }
}
