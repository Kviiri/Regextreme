/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.pattern;

import fi.helsinki.cs.kviiri.nfa.BadRegexException;
import fi.helsinki.cs.kviiri.nfa.NfaFragment;
import fi.helsinki.cs.kviiri.nfa.NfaState;
import fi.helsinki.cs.kviiri.nfa.NonDeterministicFiniteAutomaton;

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
        MyPattern mp = new MyPattern("AA*B+");
        System.out.println(mp.matches("AAAABBBB"));
        System.out.println(mp.matches("AAAAAA"));
        System.out.println(mp.matches(""));
        System.out.println(mp.matches("ABBBABB"));
        System.out.println(mp.matches("AAAAAAAAAAAABBBBBBA"));
    }
    
}
