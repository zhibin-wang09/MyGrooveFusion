package beatmaker;

import org.junit.jupiter.api.Test;
import java.io.File;
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
                a.init();
                Clip c = a.getClip();
                c.start();
                assertTrue(c.isOpen());
                c.stop();
                c.close();
            }
        }
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
                a.init();
                Clip c = a.getClip();
                c.start();
                c.stop();;
                assertFalse(c.isRunning());
                c.close();
            }
        }
    }

    @Test
    void getName() {
        File[] direc = libraryFolder.listFiles();
        assert direc != null;
        for(File f : direc){
            if(f.getName().equals(".DS_Store")) continue;
            for(File audio : Objects.requireNonNull(f.listFiles())){
                Audio a = new Audio(audio.getName(), audio);
                assertEquals(a.getName(), audio.getName());
            }
        }
    }

    @Test
    void close() {
        File[] direc = libraryFolder.listFiles();
        assert direc != null;
        for(final File f : direc){
            if(f.getName().equals(".DS_Store")) continue;
            assertNotEquals(null, f.listFiles());
            for(final File audio : Objects.requireNonNull(f.listFiles())){
                Audio a = new Audio(audio.getName(), audio);
                a.init();
                Clip c = a.getClip();
                assertTrue(c.isOpen());
                c.close();
                assertFalse(c.isActive());
            }
        }
    }

}