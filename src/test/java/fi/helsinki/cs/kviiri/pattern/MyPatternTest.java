/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.pattern;

import fi.helsinki.cs.kviiri.nfa.BadRegexException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    /*
     * Test of matches method, of class MyPattern. Tests whether matches works
     * correctly with simple equals-like test.
     */
    public void testMatchesSimpleEqualityCorrectly1() {
        assertTrue(matchesCorrectly("idkfa",
                "idkfa",
                "iddqd",
                "idfa",
                "idchoppers",
                ""));
    }

    public void testMatchesSimpleEqualityCorrectly2() {
        assertTrue(matchesCorrectly("iddqd",
                "iddqd",
                "idkfa",
                "idspispopd",
                ""));
    }

    public void testMatchesSimpleEqualityCorrectly3() {
        assertTrue(matchesCorrectly("id dq d",
                "id dq d",
                "iddqd",
                ""));
    }

    /*
     * Tests matching with maybe ? operator.
     */
    public void testMatchesMaybe1() {
        assertTrue(matchesCorrectly("idk?fa",
                "idfa",
                "idkfa",
                "idkkfa",
                "fa",
                ""));
    }
    
    public void testMatchesMaybe2() {
        assertTrue(matchesCorrectly("idcl?i?p",
                "idclip",
                "idcp",
                "idc",
                "idl",
                "idcclp",
                "ip",
                ""));
    }
    
    /*
     * Tests plus operator.
     */
    public void testMatchesWithPlusOperator1() {
        assertTrue(matchesCorrectly("idk+fa",
                "iddqd",
                "iddqd",
                "idkfa",
                "idkkkfa",
                "idfa",
                "idkfq",
                "dfka",
                "idkidkfa",
                "fa",
                ""));
    }

    public void testMatchesWithPlusOperator2() {
        assertTrue(matchesCorrectly("idcho+ppe+rs",
                "idchoppers",
                "idchoooppeeeeeers",
                "idcpprs",
                "idchoidchoidchoppeppeppers",
                "idkfa",
                ""));
    }

    /*
     * Tests star operation.
     */
    public void testMatchesStarOperation1() {
        assertTrue(matchesCorrectly("idbehold*a",
                "idbehola",
                "idbeholdddda",
                "a",
                "dddddda",
                "idbeholda",
                ""));
    }

    public void testMatchesStarOperation2() {
        assertTrue(matchesCorrectly("idsp*is*popd",
                "idspppppisssspopd",
                "idsiopd",
                "idspppipopd",
                "isspopd",
                "popd",
                ""));

    }

    /*
     * Tests union |
     */
    public void testMatchesUnion1() {
        assertTrue(matchesCorrectly("idkfa|iddqd",
                "idkfa",
                "iddqd",
                "idkfa|iddqd",
                "iddfa",
                ""));
    }

    public void testMatchesUnion2() {
        assertTrue(matchesCorrectly("idclip|idkfa|idspispopd",
                "idclip",
                "idkfa",
                "idspispopd",
                "idcla",
                "idclipidkfaidspispopd",
                ""));
    }
/*
 * Tests star and plus combinations.
 */
public void testMatchesStarAndPlus1() {
        assertTrue(matchesCorrectly("id+spis*popd",
                "ididspisspispopd",
                "spispopd",
                "idspisidspispopd",
                "idddspisssspopd",
                "idspipopd",
                "ispipopd",
                ""));
    }
    
    public void testMatchesStarAndPlus2() {
        assertTrue(matchesCorrectly("cyb*erdem*o+n",
                "cyerdeon",
                "cybcyberdemon",
                "cybbbberden",
                "cybberdemmmooon",
                "cyerdemon",
                "cyberdemon",
                "cybererdemerdemon",
                ""));
    }
    
    /*
     * Test parenthesis functionality
     */
    public void testMatchesParentheses1() {
        assertTrue(matchesCorrectly("id(spis)+popd",
                "idspispopd",
                "id(spis)popd",
                "idspissspopd",
                "idspisspispopd",
                "idpopd",
                ""));
    }
    
    public void testMatchesParentheses2() {
        assertTrue(matchesCorrectly("idc(l+i)*p",
                "idclip",
                "idcp",
                "idcilip",
                "idciiiiip",
                "idcllllllillllp",
                "idclllip",
                ""));
    }
    
    
    public void testMatchesParentheses3() {
        assertTrue(matchesCorrectly("id*(dqd|clip)+",
                "dqd",
                "dqddqddqd",
                "clip",
                "clipclipclip",
                "iddqd",
                "idclip",
                "idclipclipclip",
                "iddqddqd",
                "idspispopd",
                ""));
    }
    
    /*
     * Tests character groups
     */
    
    public void testMatchesGroup1() {
        assertTrue(matchesCorrectly("[id]a",
                "ia",
                "da",
                "ida",
                "a",
                ""));
        
    }
    
    public void testMatchesGroup2() {
        assertTrue(matchesCorrectly("[aeiouyäö][bcdfghjklmnpqrstvwxyz]",
                "aw",
                "es",
                "äx",
                "aebc",
                "b",
                ""));
    }
    
    public void testMatchesGroup3() {
        assertTrue(matchesCorrectly("(-?[123456789][0123456789]*|0)",
                "-512",
                "42",
                "7",
                "-1",
                "0",
                "asb",
                "-0",
                "dirlandaa",
                "0123456789",
                ""));
    }
    
    /*
     * matchesCorrectly simply tests MyPattern versus Java's standard regex classes
     * Pattern and Matcher. True is returned iff MyPattern matches those and only those
     * testWord Strings also matched by Java's regex tools.
     */
    private boolean matchesCorrectly(String regex, String... testWords) {
        Pattern referencePattern = Pattern.compile(regex);
        MyPattern actualPattern;
        try {
            actualPattern = new MyPattern(regex);
        } catch (BadRegexException ex) {
            return false;
        }
        for (String s : testWords) {
            Matcher m = referencePattern.matcher(s);
            if(m.matches() != actualPattern.matches(s)) {
                return false;
            }
        }
        return true;
    }
}
