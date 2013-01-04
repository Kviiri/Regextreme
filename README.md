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

TODO: documentation, code readability


Update 16.7.2012
Adding subautomata works, somewhat - operator precedence does not.

TODO: More unit tests.