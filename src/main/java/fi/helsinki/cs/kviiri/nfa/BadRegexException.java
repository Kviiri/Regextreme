/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.nfa;

/**
 *
 * @author kviiri
 */
public class BadRegexException extends RuntimeException {
    public BadRegexException(String msg) {
        super(msg);
    }
}
