/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.kviiri.nfa;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 *
 * @author kviiri
 */
class IntegerStack {
    private int[] stack;
    private int sp = 0;
    
    /**
     * Creates a new integer stack of the specified size
     * @param size 
     */
    public IntegerStack(int size) {
        stack = new int[size];
    }
    /**
     * Creates a new integer stack of size 128
     */
    public IntegerStack() {
        
        this(128);
    }
    
    
    /**
     * Pushes i on top of the stack. If stack is full, its size is doubled.
     * @param i 
     */
    public void push(int i) {
        if(sp + 1 == stack.length) {
            stack = Arrays.copyOf(stack, stack.length * 2);
        }
        stack[sp] = i;
        sp++;
    }
    
    /**
     * Returns the top value of the stack
     * @return 
     */
    public int peek() {
        if(sp == 0) throw new EmptyStackException();
        return stack[sp - 1];
    }
    
    /**
     * Removes and returns the top value of the stack.
     * @return 
     */
    public int pop() {
        if(sp == 0) throw new EmptyStackException();
        return stack[--sp];
    }
    
    /**
     * Returns true iff stack is empty
     * @return 
     */
    public boolean isEmpty() {
        return sp == 0;
    }
}
