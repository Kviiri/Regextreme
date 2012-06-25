/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.nfa;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author kviiri
 * 
 * Represents a Nondeterministic finite automaton (NFA).
 * Formally, the NFA is a quintuple:
 * <ul>
 *  <li>A set of states (Q)</li>
 *  <li>A set of input symbols</li>
 *  <li>The transition relation from a State-symbol pair to some subset of Q</li>
 *  <li>An initial state</li>
 *  <li>A set of accepting/final states</li>
 * </ul>
 * 
 * This implementation doesn't contain a separate set for input symbols, but
 * should nevertheless work in the proper fashion.
 */
public class NonDeterministicFiniteAutomaton {
    private State initialState;
    private Set<State> finalState;
    
    
    /**
     * Creates a NFA that matches any String that matches the regex in s.
     * Currently supported operations beyond char-by-char:
     * <ul>
     *  <li>+ operation (at least once)</li>
     * </ul>
     * @param s 
     */
    public NonDeterministicFiniteAutomaton(String s) {
        initialState = new State();
        State currentState = initialState;
        for(int i = 0; i < s.length(); i++) {
            State nextState = new State();
            //One or more times operator +
            if(s.charAt(i) == '+' && i > 0) {
                currentState.addTransition(s.charAt(i-1), currentState);
                i++;
            }
            currentState.addTransition(s.charAt(i), nextState);
            currentState = nextState;
        }
        finalState = new HashSet<State>();
        finalState.add(currentState);
    }
    
    
    public boolean accepts(String input) {
        Set<State> currentStates = new HashSet<State>();
        Set<State> nextStates = new HashSet<State>();
        currentStates.add(initialState);
        for(int i = 0; i < input.length(); i++) {
            for(State s : currentStates) {
                nextStates.addAll(s.getTransition(input.charAt(i)));
            }
            if(nextStates.isEmpty()) return false;  //All paths failed, return false.
            
            //Swapping the two Sets
            Set<State> swap = currentStates;
            currentStates = nextStates;
            nextStates = swap;
            nextStates.clear();
        }
        for(State s : currentStates) {
            if(finalState.contains(s)) return true;
        }
        return false;
    }
}
