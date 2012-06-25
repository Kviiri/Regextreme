/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.pattern;

import fi.helsinki.cs.kviiri.nfa.NonDeterministicFiniteAutomaton;

/**
 *
 * @author kviiri
 * 
 * A Pattern class that compiles a regular expression into a NonDeterministicFiniteAutomaton.
 */
public class MyPattern {
    
    private NonDeterministicFiniteAutomaton nfa;
    
    /**
     * Compiles the input String into an automaton.
     * @param s
     * @return 
     */
    public MyPattern(String s) {
        nfa = new NonDeterministicFiniteAutomaton(s);
    }
    
    public boolean matches(String input) {
        return nfa.accepts(input);
    }
    
    public static void main(String[] args) {
        MyPattern mp = new MyPattern("A+B+A");
        System.out.println(mp.matches("AAA"));
        System.out.println(mp.matches("AB+A"));
        System.out.println(mp.matches("AAAAAAAAAAAABBBBBBA"));
    }
    
}
