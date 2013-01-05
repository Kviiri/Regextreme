/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.nfa;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a piece of NFA
 * @author kviiri
 */
public class NfaFragment {

    //the initial state
    private NfaState start;
    //the states with "dangling" arrows
    private List<NfaState> terminals;

    public NfaFragment(NfaState start, List<NfaState> terminals) {
        this.start = start;
        this.terminals = terminals;
    }

    /**
     * Patches this and next together by adding a transition from each "terminal"
     * to the next state. As a result, this fragment's only remaining terminal
     * will be "next".
     * @param next 
     */
    private void connect(NfaState next) {
        for (NfaState n : terminals) {
            if (n.getaState() == null) {
                n.setaState(next);
            } else {
                n.setbState(next);
            }
        }
        terminals.clear();
        terminals.add(next);
    }

    /**
     * Creates a NFA based on the parameter regex.
     * @param regex
     * @return the NfaState that is the initial state of the NFA.
     * @throws BadRegexException
     */
    public static NfaState createNfa(String regex) throws BadRegexException {
        //Convert the regex to a postfix regex
        regex = postfixify(regex);
        Stack<NfaFragment> stack = new Stack<NfaFragment>();
        NfaState match = new NfaState(NfaState.MATCH, null, null);

        //Go through the postfix regex char by char
        for (int i = 0; i < regex.length(); i++) {
            
            switch (regex.charAt(i)) {
                //Escape character!
                case '\\':
                    //escape by stepping forward a character and not continuing
                    i++;
                    break;
                case '+':
                    stack.peek().makePlus();
                    continue;
                case '*':
                    stack.peek().makeStar();
                    continue;
                case '?':
                    stack.peek().makeMaybe();
                    continue;
                case '|':
                    stack.push(stack.pop().makeUnion(stack.pop()));
                    continue;
                case '~':   //using tilde as concat operator for postfixes
                    stack.push(stack.pop().makeConcat(stack.pop()));
                    continue;
                case '.':   //there's the any key!
                    NfaState s = new NfaState(NfaState.ANY, null, null);
                    NfaFragment frag = new NfaFragment(s, new ArrayList<NfaState>());
                    frag.terminals.add(s);
                    stack.push(frag);
                    continue;
            }
            //We come here only if the character is literal
            //Or the last one was escape \
            if (regex.length() == i) {
                throw new BadRegexException("One does not simply put a lonely "
                        + "escape character in the end of the regex!");
            }
            //For a literal, we create a new NfaState and a Fragment containing it
            NfaState s = new NfaState(regex.charAt(i), null, null);
            NfaFragment frag = new NfaFragment(s, new ArrayList<NfaState>());
            frag.terminals.add(s);
            //And on the stack it goes
            stack.push(frag);
        }
        stack.peek().connect(match);
        return stack.peek().start;
    }

    private void makePlus() {
        NfaState next = new NfaState(NfaState.SPLIT, start, null);
        connect(next);
    }

    private void makeStar() {
        NfaState next = new NfaState(NfaState.SPLIT, start, null);
        connect(next);
        start = next;
    }

    /**
     * Makes the NfaFragment a "maybe" fragment (?)
     *
     */
    private void makeMaybe() {
        NfaState next = new NfaState(NfaState.SPLIT, start, null);
        start = next;
        terminals.add(next);
    }

    /**
     * Creates a new NfaFragment with a split state with epsilons to this and
     * other
     *
     * @param other
     * @return
     */
    private NfaFragment makeUnion(NfaFragment other) {
        NfaState next = new NfaState(NfaState.SPLIT, start, other.start);
        List<NfaState> terminalList = new ArrayList<NfaState>(terminals);
        terminalList.addAll(other.terminals);
        return new NfaFragment(next, terminalList);
    }

    /**
     * Creates a new NfaFragment with this concatenated after other
     *
     * @param other
     * @return
     */
    private NfaFragment makeConcat(NfaFragment other) {
        other.connect(start);
        return new NfaFragment(other.start, terminals);
    }

    /**
     * Converts a regular expression to a postfix-regex.
     * @param regex
     * @return
     * @throws BadRegexException 
     */
    private static String postfixify(String regex) throws BadRegexException {
        //Unroll the groups of the regex
        regex = unrollGroups(regex);
        //ret will house the postfix expression as it is being built
        StringBuilder ret = new StringBuilder();
        IntegerStack stackOfAlts = new IntegerStack(16);
        IntegerStack stackOfNonOps = new IntegerStack(16);

        int alts = 0; //we keep track of number of alternations and literals
        int nonOps = 0;

        for (int i = 0; i < regex.length(); i++) {
            switch (regex.charAt(i)) {
                case '\\':
                    if (i + 1 == regex.length()) {
                        throw new BadRegexException("Lonely escape char at " + i);
                    }
                    i++;
                    ret.append('\\');
                    ret.append(regex.charAt(i));
                    nonOps++;
                    continue;
                case '(':
                    //new subexpression!
                    //any previously existing literals will need to be concatenated
                    if (nonOps > 1) {
                        nonOps--;
                        ret.append("~");
                    }
                    //put away these variables on the stack
                    stackOfAlts.push(alts);
                    stackOfNonOps.push(nonOps);
                    alts = 0;
                    nonOps = 0;
                    continue;
                case ')':
                    //end subexpression
                    
                    //mismatched parentheses
                    if (stackOfAlts.isEmpty()) {
                        throw new BadRegexException("Mismatched parentheses!");
                    }
                    //any nonops inside the subexpression will need to be concat'd
                    while (--nonOps > 0) {
                        ret.append('~');
                    }
                    //any alternations, likewise
                    while (alts > 0) {
                        ret.append('|');
                        alts--;
                    }
                    //restore the values of alts and nonOps from the stacks
                    alts = stackOfAlts.pop();
                    nonOps = stackOfNonOps.pop();
                    //The parenthesized subexp is now a nonOp
                    nonOps++;
                    continue;
                case '|':
                    //alternation
                    //mismatches if there is no subexpression to alternate
                    if (nonOps == 0) {
                        throw new BadRegexException("Regex error: misplaced "
                                + "alternation at " + i);
                    }
                    //concatenate the subexpression
                    while (--nonOps > 0) {
                        ret.append('~');
                    }
                    alts++;
                    continue;
                case '~':
                    //tilde is the concat operation in my postfix-regex
                    //it must be automatically escaped
                    ret.append('\\').append('~');
                    nonOps++;
                    continue;
                case '+':
                case '?':
                case '*':
                    //these operators need a subexpression. without one, they fail
                    if (nonOps == 0) {
                        throw new BadRegexException("Lonely operand at " + i);
                    }
                    //they'll be appended without any extras
                    ret.append(regex.charAt(i));
                    continue;
            }
            //we came here for literals (or escaped characters)
            //previous subexpression? concat it
            if (nonOps > 1) {
                nonOps--;
                ret.append('~');
            }
            //finally, append the literal
            ret.append(regex.charAt(i));
            //it's now a new subexpression
            nonOps++;
        }
        //finally, we concat all remaining subexpressions
        while (--nonOps > 0) {
            ret.append('~');
        }
        
        //and append the remaining alternations
        while (alts > 0) {
            ret.append('|');
            alts--;
        }
        return ret.toString();
    }

    /*
     * TODO: Allow using ranges like [a-z] and negation like [a-z^asd]
     */
    /**
     * Unrolls groups in regex: "[abcd] -> (a|b|c|d)
     * @param regex
     * @return regex with groups unrolled
     */
    private static String unrollGroups(String regex) {
        //ret will house the unrolled group as it is being created
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            //[ marks the beginning of a group
            if (regex.charAt(i) == '[') {
                //add opening paren, step forward a char
                ret.append('(');
                i++;
                //until the end of the group
                while (regex.charAt(i) != ']') {
                    //add the char with an alt
                    ret.append(regex.charAt(i));
                    ret.append('|');
                    i++;
                }
                ret.deleteCharAt(ret.length() - 1); //the last | is not needed
                ret.append(')');
            } else {
                ret.append(regex.charAt(i));
            }
        }
        return ret.toString();
    }
}
