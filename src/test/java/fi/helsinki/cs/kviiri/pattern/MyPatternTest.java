/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.pattern;

import junit.framework.TestCase;

/**
 *
 * @author kviiri
 */
public class MyPatternTest extends TestCase {
    
    public MyPatternTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test of matches method, of class MyPattern.
     * Tests whether matches works correctly with simple equals-like test.
     */
    public void testMatchesDoesSimpleEqualityCorrectly() {
        MyPattern instance = new MyPattern("idkfa");
        assertTrue(instance.matches("idkfa"));
        assertFalse(instance.matches("idfkfaa"));
        assertFalse(instance.matches("idKFa"));
        assertFalse(instance.matches("iddqd"));
        assertFalse(instance.matches(""));
        assertFalse(instance.matches("idkfa+"));
    }
    public void testMatchesDoesPlusOperatorCorrectly() {
        MyPattern instance = new MyPattern("idkf+a");
        assertTrue(instance.matches("idkfa"));
        assertTrue(instance.matches("idkffffffa"));
        assertFalse(instance.matches("idkFfa"));
        assertFalse(instance.matches("idkf+a"));
        assertFalse(instance.matches("idka"));
    }
}
