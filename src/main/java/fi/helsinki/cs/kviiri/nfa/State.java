/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.nfa;

import java.util.*;

/**
 *
 * @author kviiri
 *
 * Represents a single state in the NonDeterministicFiniteAutomaton. Contains
 * its own transition map (Character -> Set&ltState&gt)
 * Package-private like a boss.
 */
class State {

    private Map<Character, Set<State>> transition;

    
    /**
     * Constructs a new State that uses a blank transition map.
     */
    State () {
        transition = new HashMap<Character, Set<State>>();
    }
    /**
     * Constructs a new State that uses the specified Map as a transition table.
     * @param transition 
     */
    State (Map<Character, Set<State>> transition) {
        this.transition = transition;
    }
    
    /**
     * Adds the specified transition to the transition map.
     * @param c The input Character
     * @param s The next State
     */
    void addTransition(Character c, State s) {
        if(transition.containsKey(c)) {
            transition.get(c).add(s);
        }
        else {
            Set<State> newSet = new HashSet<State>();
            newSet.add(s);
            transition.put(c, newSet);
        }
    }
    
    /**
     * Adds transitions from the input character c to all states.
     * @param c
     * @param states 
     */
    void addAllTransitions(Character c, Collection<State> states) {
        for(State s : states) {
            addTransition(c, s);
        }
    }
    
    /**
     * @param c
     * @return The transitions from this State with the symbol c (empty Set if none)
     */
    Set<State> getTransition(char c) {
        if(!transition.containsKey(c)) return new HashSet<State>();
        return transition.get(c);
    }
}
