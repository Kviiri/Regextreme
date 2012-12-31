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
        currentStates.add(this);
        Set<NfaState> nextStates = new HashSet<NfaState>();
        Set<NfaState> resolvedSplits = new HashSet<NfaState>();
        //This loop goes through the characters in the String
        for (char ch : s.toCharArray()) {
            /*
             * This loop exists to allow using epsilon transitions (SPLITs)
             * without consuming characters.
             */
            while (!currentStates.isEmpty()) {

                /*
                 * Next, we use an Iterator to go through the set of current
                 * states
                 */
                Iterator<NfaState> it = currentStates.iterator();
                while (it.hasNext()) {
                    NfaState state = it.next();
                    if (state.c == NfaState.SPLIT) {
                        resolvedSplits.add(state.getaState());
                        resolvedSplits.add(state.getbState());
                    } else if (state.c == ch) {
                        nextStates.add(state.getaState());

                    }
                    it.remove();

                }
                currentStates.addAll(resolvedSplits);
                resolvedSplits.clear();
            }
            Set<NfaState> swap = currentStates;
            currentStates = nextStates;
            nextStates = swap;
            //No need to clear nextStates I hope. It should be cleared after the last loop

        }
        while (!currentStates.isEmpty()) {
            Iterator<NfaState> it = currentStates.iterator();
            while (it.hasNext()) {
                NfaState state = it.next();
                if (state.c == NfaState.MATCH) {
                    return true;
                }
                if (state.c == NfaState.SPLIT) {
                    resolvedSplits.add(state.aState);
                    resolvedSplits.add(state.bState);
                }
                it.remove();
            }
            currentStates.addAll(resolvedSplits);
            resolvedSplits.clear();
        }
        return false;
    }
}
