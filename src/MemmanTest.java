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
}
