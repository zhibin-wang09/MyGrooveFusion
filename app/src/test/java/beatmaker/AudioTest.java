package beatmaker;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.sql.SQLOutput;
import java.util.Objects;
import javax.sound.sampled.Clip;

import static org.junit.jupiter.api.Assertions.*;

class AudioTest {
    final File libraryFolder = new File("./src/test/resources/audios");
    @Test
    void play() {
        File[] direc = libraryFolder.listFiles();
        assert direc != null;
        for(final File f : direc){
            if(f.getName().equals(".DS_Store")) continue;
            assertNotEquals(null, f.listFiles());
            for(final File audio : Objects.requireNonNull(f.listFiles())){
                Audio a = new Audio(audio.getName(), audio);
                a.play();
                Clip c = a.getClip();
                assert c != null;
                assertTrue(c.isOpen());
                c.stop();
                c.close();
            }
        }
    }

    @Test
    void resume() {
    }

    @Test
    void stop() {
        File[] direc = libraryFolder.listFiles();
        assert direc != null;
        for(final File f : direc){
            if(f.getName().equals(".DS_Store")) continue;
            assertNotEquals(null, f.listFiles());
            for(final File audio : Objects.requireNonNull(f.listFiles())){
                Audio a = new Audio(audio.getName(), audio);

            }
        }
    }

    @Test
    void getName() {
    }

    @Test
    void close() {
    }

}