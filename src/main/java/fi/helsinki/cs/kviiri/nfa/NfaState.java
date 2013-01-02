/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.nfa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author kviiri
 */
public class NfaState {

    public static final int SPLIT = 32768;
    public static final int MATCH = 32769;
    int c;
    private NfaState aState;
    private NfaState bState;

    public NfaState(int c, NfaState aState, NfaState bState) {
        this.c = c;
        this.aState = aState;
        this.bState = bState;
    }

    public NfaState getaState() {
        return aState;
    }

    public NfaState getbState() {
        return bState;
    }

    public void setaState(NfaState aState) {
        this.aState = aState;
    }

    public void setbState(NfaState bState) {
        this.bState = bState;
    }

    public boolean accepts(String s) {
        Set<NfaState> currentStates = new HashSet<NfaState>();
        addSplit(this, currentStates);
        Set<NfaState> nextStates = new HashSet<NfaState>();
        
        for(char ch : s.toCharArray()) {
            for(NfaState state : currentStates) {
                if(state.c == ch) {
                    addSplit(state.getaState(), nextStates);
                }
            }
            Set<NfaState> swap = currentStates;
            currentStates = nextStates;
            nextStates = swap;
            nextStates.clear();
        }
        for(NfaState state : currentStates) {
            if(state.c == NfaState.MATCH) return true;
        }
        
        return false;
    }

    private void addSplit(NfaState state, Set<NfaState> set) {
        if (state.c == NfaState.SPLIT) {
            addSplit(state.getaState(), set);
            addSplit(state.getbState(), set);
        } else {
            set.add(state);
        }
    }
}
