/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.pattern;

import fi.helsinki.cs.kviiri.nfa.BadRegexException;
import fi.helsinki.cs.kviiri.nfa.NfaFragment;
import fi.helsinki.cs.kviiri.nfa.NfaState;

/**
 *
 * @author kviiri
 * 
 * A Pattern class that compiles a regular expression into a NonDeterministicFiniteAutomaton.
 */
public class MyPattern {
    
    private NfaState nfa;
    
    /**
     * Compiles the input String into an automaton.
     * @param s
     * @return 
     */
    public MyPattern(String s) throws BadRegexException {
        nfa = NfaFragment.createNfa(s);
    }
    
    public boolean matches(String input) {
        return nfa.accepts(input);
    }
    
    public static void main(String[] args) throws BadRegexException {
        MyPattern mp = new MyPattern("a(b|c)");
        System.out.println(mp.matches("ac"));
    }
    
}
