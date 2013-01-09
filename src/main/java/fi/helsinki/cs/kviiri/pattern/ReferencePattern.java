/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author kviiri
 */
public class ReferencePattern implements RegexPattern {
    private Pattern p;
    
    public ReferencePattern(String regex) {
        p = Pattern.compile(regex);
        
    }
    public boolean matches(String input) {
        Matcher m = p.matcher(input);
        return m.matches();
    }
    
}
