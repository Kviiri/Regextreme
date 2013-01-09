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
public class MyPattern implements RegexPattern {
    
    private NfaState nfa;
    
    /**
     * Compiles the input String into an automaton.
     * @param s
     */
    public MyPattern(String s) throws BadRegexException {
        nfa = NfaFragment.createNfa(s);
    }
    
    /**
     * Returns true if and only if the input String matches the MyPattern.
     * @param input
     * @return
     */
    public boolean matches(String input) {
        return nfa.accepts(input);
    }
}
