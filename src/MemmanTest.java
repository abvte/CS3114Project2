import student.TestCase;

/**
 * @author Jinwoo Yom
 * @version 1.0
 */
public class MemmanTest extends TestCase {
    /**
     * This method sets up the tests that follow.
     */
    public void setUp() {
        // Nothing Here
    }
    
    /**
     * This method gets code coverage of the class declaration.
     */
    public void testMInit() {
        Memman mem = new Memman();
        assertNotNull(mem);
        Memman.main(null);
    }
    
    public void testFirstError() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"aaaaa"};
        Memman.main(args);
    }
    
    public void testError() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "aaaaa"};
        Memman.main(args);
    }
    
    public void testWrongCommand() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "errorFile.txt"};
        Memman.main(args);
    }
    
    public void testParsing() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "testFile.txt"};
        Memman.main(args);
    }
    
    public void testMemoryManager(){
    	
    }
}
