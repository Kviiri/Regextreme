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
    
    public IntegerStack(int size) {
        stack = new int[size];
    }
    public IntegerStack() {
        this(128);
    }
    
    public void push(int i) {
        if(sp + 1 == stack.length) {
            stack = Arrays.copyOf(stack, stack.length * 2);
        }
        stack[sp] = i;
        sp++;
    }
    
    public int peek() {
        if(sp == 0) throw new EmptyStackException();
        return stack[sp - 1];
    }
    
    public int pop() {
        if(sp == 0) throw new EmptyStackException();
        return stack[--sp];
    }
    
    public boolean isEmpty() {
        return sp == 0;
    }
}
