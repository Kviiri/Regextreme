/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.nfa;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author kviiri
 *
 * Represents a Nondeterministic finite automaton (NFA). Formally, the NFA is a
 * quintuple: <ul> <li>A set of states (Q)</li> <li>A set of input symbols</li>
 * <li>The transition relation from a State-symbol pair to some subset of Q</li>
 * <li>An initial state</li> <li>A set of accepting/final states</li> </ul>s
 *
 * This implementation doesn't contain a separate set for input symbols, but
 * should nevertheless work in the proper fashion.
 */
public class NonDeterministicFiniteAutomaton {

    private State initialState;
    private Set<State> finalState;

    /**
     * Creates a NFA that matches any String that matches the regex in s.
     * Currently supported operations beyond char-by-char: <ul> <li>+ operation
     * (at least once)</li> </ul>
     *
     * @param s
     */
    public NonDeterministicFiniteAutomaton(String s) {
        try {
            validateRegex(s);
        } catch (BadRegexException ex) {
            System.out.println("The Regex syntax was bad: " + ex.getMessage());
        }
        initialState = new State();
        State currentState = initialState;
        int i = 0;
        int amountParsed = 0;
        while (amountParsed < s.length()) {
            //read String until an operator is found or the String is exhausted
            if (isOperator(s.charAt(i)) || i + 1 == s.length()) {
                currentState = appendAutomaton(s.substring(amountParsed, i + 1), currentState);
                amountParsed = i + 1;
            }
            i++;
        }
        finalState = new HashSet<State>();
        finalState.add(currentState);
    }

    private void validateRegex(String s) throws BadRegexException {
        if (s.length() == 0) {
            throw new BadRegexException("Regex must not be empty!");
        }
        //Don't start your regexps with operators
        if (isOperator(s.charAt(0))) {
            throw new BadRegexException("Regex must not start with an operator!");
        }

        //Make sure there aren't operators after operators
        for (int i = 1; i < s.length() - 1; i++) {
            if (isOperator(s.charAt(i)) && isOperator(s.charAt(i + 1))) {
                throw new BadRegexException("Regex must not have operators after operators!");
            }
        }
    }

    private boolean isOperator(char c) {
        return (c == '+' || c == '*' || c == '|');
    }

    public boolean accepts(String input) {
        Set<State> currentStates = new HashSet<State>();
        Set<State> nextStates = new HashSet<State>();
        currentStates.add(initialState);
        for (int i = 0; i < input.length(); i++) {
            addEpsilonStates(currentStates);
            for (State s : currentStates) {
                nextStates.addAll(s.getTransition(input.charAt(i)));
            }
            if (nextStates.isEmpty()) {
                return false;  //All paths failed, return false.
            }
            //Swapping the two Sets
            Set<State> swap = currentStates;
            currentStates = nextStates;
            nextStates = swap;
            nextStates.clear();
        }
        addEpsilonStates(currentStates);
        for (State s : currentStates) {
            if (finalState.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Appends a basic, Star, or Plus automaton based on the operator found in
     * the String.
     *
     * @param s
     * @return
     */
    private State appendAutomaton(String s, State state) {
        switch (s.charAt(s.length() - 1)) {
            case '*':
                return appendStarAutomaton(s.substring(0, s.length() - 1), state);
            case '+':
                return appendPlusAutomaton(s.substring(0, s.length() - 1), state);
            default:
                return appendBasicAutomaton(s.substring(0, s.length()), state);
        }
    }

    /**
     * Appends a NFA based on the input String to the specified State. Doesn't
     * actually create a new NFA instance, but only converts a String into
     * States and returns the last State.
     *
     * @param s
     * @param state
     * @return A new State following the appended automaton.
     */
    private State appendBasicAutomaton(String s, State state) {
        State currentState = state;
        State nextState;
        for (int i = 0; i < s.length(); i++) {
            nextState = new State();
            currentState.addTransition(s.charAt(i), nextState);
            currentState = nextState;
        }
        return currentState;
    }

    /**
     * Creates a new Star NFA based on the input String and appends it to the
     * given State.
     *
     * @param s
     * @param state
     * @return A new State following the appended automaton.
     */
    private State appendStarAutomaton(String s, State state) {
        NonDeterministicFiniteAutomaton nfa = new NonDeterministicFiniteAutomaton(s);

        /*
         * Allow skipping the automaton altogether by creating an epsilon from
         * the starting State to the post-automaton State. Also permit looping
         * by adding transitions from nfa's accepting States to the beginning
         * State.
         */

        state.addEpsilonTransition(nfa.initialState);
        State newState = new State();
        for (State accept : nfa.finalState) {
            accept.addEpsilonTransition(state);
        }
        state.addEpsilonTransition(newState);
        return newState;
    }

    /**
     * Creates a new Plus NFA based on the input String and appends it to the
     * given State.
     *
     * @param s
     * @param state
     * @return A new State following the appended automaton.
     */
    private State appendPlusAutomaton(String s, State state) {
        NonDeterministicFiniteAutomaton nfa = new NonDeterministicFiniteAutomaton(s);
        /*
         * Permit looping by adding transitions from the new post-automaton State
         * to the automaton's initial State.
         */
        
        state.addEpsilonTransition(nfa.initialState);
        State newState = new State();
        for (State accept : nfa.finalState) {
            accept.addEpsilonTransition(newState);
        }
        newState.addEpsilonTransition(nfa.initialState);
        return newState;
    }

    private void addEpsilonStates(Set<State> currentStates) {
        Set<State> generation = new HashSet<State>();
        while (!generation.equals(currentStates)) {
            generation.addAll(currentStates);
            for (State s : generation) {
                currentStates.addAll(s.getEpsilonTransitions());
            }
        }
    }
}
