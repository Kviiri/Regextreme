Regextreme
==========

Parsing regular expressions for fun and profit.

Update 4.1.2013
Modified the code to feature more Thompson's construction and less wonky improvisation. Supported features:

 - Operators *, +, | and ?
 - Subexpressions delimited by parentheses ()
 - Character groups without intervals, negation
 - Period as "any char" -metachar
 - \ as escape character, any character after it is interpreted as a literal character (note, not the same as in typical regex!)
 - Unit tests


Description of the algorithm:

This implementation is based on the Thompson regex to NFA construction algorithm, with some added features. The construction is carried out in several steps:

The automaton created is composed of NfaStates. Each state has the following properties:
 - c, which can either be a character, SPLIT, MATCH or ANY
 - two NfaState references. A is the "normal" successor for non-SPLIT states, and for SPLIT states, A and B are both reached through epsilon transition (no symbol consumed)

First, the regex has its groups unrolled using the alternation operator |. For example, [abc] becomes the equivalent (a|b|c).

Second, the regex is converted into a postfix regex, as in Thompson's paper. Tilde (~) is used as an explicit concatenation operator. For example, the regex "(ab|cd)" becomes "ab~cd~|". Tilde characters present in the original regex will be escaped automatically: the regex "a~+" will become "a\~+~" in postfix form.

The resulting postfix regex will be compiled into a NFA by creating "fragments" and storing them on a stack. The regex is evaluated character by character:

 - if the character is \, the next character will be treated as literal
 - if the character is +, the fragment on the top of the stack is modified to allow repetition
 - if the character is ?, the fragment on the top of the stack is modified to allow skipping
 - if the character is *, the fragment on the top of the stack is modified to allow repetition and skipping
 - if the character is |, a new fragment is created. This fragment consists of a splitting state. The stack is popped twice, with the first returned fragment being the split state's A branch and the second returned fragment the B branch. The new fragment is then pushed on the stack.
 - if the character is ~, the two topmost fragments are taken from the stack and will be joined to form a single fragment (concatenation).
 - if the character is . (period), a new fragment with the "ANY" c value will be pushed on the stack
 - if the character is a literal, it will be pushed on top of the stack as a new fragment

After the postfix is parsed, there is only one fragment on the stack. The automaton is perfected by creating an accepting state and appending it to the automaton.


The automaton's purpose is to determine whether an input String is accepted or not. During the simulation, the automaton maintains a set of states it is currently in as well as a set of states it will be on the next step. The set of current states initially contains only the initial states or states reachable from it via epsilon transition.

The automaton is simulated by reading the input string character by character. For each character, the automaton checks all the states it is currently in. If a state's c value matches the character currently being read, or is the special ANY value, the state's A successor is added to the set of next states. After all states have been checked, the set of current states is swapped with the set of next states and the set of next states is cleared. If the set of current states is empty, the string doesn't match and the algorithm halts.

Whenever next states are added to sets, an algoritm is used to make sure that instead of SPLIT states, their successors are added to the set. This is done recursively - if a state being added is a split state, instead its successors A and B are added using the same algoritm. If a state is not a split state, it is added normally.

Finally, once the input string has been read completely, all the states are checked. If any of them is a matching state, the algoritm accepts the string - otherwise not.