import com.stringschecker.FileManipulator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FileManipulatorTests {

    static final String pathToFirstFile = "src/test/resources/first.txt";
    static final String pathToSecondFile = "src/test/resources/second.txt";
    static final String pathToThirdFile = "src/test/resources/second.txt";
    FileManipulator manipulator;

    static final Set<String> readTestCorrectSet = Set.of(
            "\"79029515044\";\"79747701630\"",
            "\"79116168019\";\"79940451649\";\"79623162625\""
            );

    @Test(expected = RuntimeException.class)
    public void testThatEmptyFileWasRead_expectRuntimeException() throws FileNotFoundException {
        manipulator = new FileManipulator(new File(pathToSecondFile));
        manipulator.read();
    }

    @Test(expected = RuntimeException.class)
    public void testReadNonExistentFile_expectRuntimeException() throws FileNotFoundException {
        manipulator = new FileManipulator(new File(pathToThirdFile));
        manipulator.read();
    }

    @Test
    public void testThatCorrectFileWasReadAndParsedCorrectly() throws FileNotFoundException {
        manipulator = new FileManipulator(new File(pathToFirstFile));
        manipulator.read();
        Assert.assertEquals(readTestCorrectSet, manipulator.getFilteredSetOfEntries());
    }

    @AfterEach
    public void flushManipulator (){
        manipulator = null;
    }
}
