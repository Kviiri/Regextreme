package fi.helsinki.cs.kviiri.pattern;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Measures the time taken to match with RegexPatterns.
 * @author kviiri
 */
public class SpeedTester {
    private RegexPattern pat;
    
    public SpeedTester(RegexPattern pat) {
        this.pat = pat;
    }
    
    public long getMatchTime(String s) {
        long startTime = System.currentTimeMillis();
        pat.matches(s);
        return System.currentTimeMillis() - startTime;
    }
    
    
    public static void main(String[] args) {
        //The first testcase is a known "pathological" case in Perl regexes
        long myCreationTime = System.currentTimeMillis();
        RegexPattern myPat = new MyPattern("a?a?a?a?a?a?a?a?a?a?"
                + "a?a?a?a?a?a?a?a?a?a?aaaaaaaaaaaaaaaaaaaa");  //That's twenty a? and a
        System.out.println("My creation time: " + (System.currentTimeMillis() - myCreationTime));
        long refCreationTime = System.currentTimeMillis();
        RegexPattern refPat = new ReferencePattern("a?a?a?a?a?a?a?a?a?a?"
                + "a?a?a?a?a?a?a?a?a?a?aaaaaaaaaaaaaaaaa");
        System.out.println("Ref creation time: " + (System.currentTimeMillis() - refCreationTime));
        SpeedTester myTest = new SpeedTester(myPat);
        SpeedTester refTest = new SpeedTester(refPat);
        StringBuilder sb = new StringBuilder();
        long myMatchTimes = 0;
        long refMatchTimes = 0;
        for(int i = 0; i < 40; i++) {
            sb.append('a');
            System.out.println("Round " + (i+1) + " : ");
            myMatchTimes += myTest.getMatchTime(sb.toString());
            System.out.println("Mine: " + myMatchTimes);
            refMatchTimes += refTest.getMatchTime(sb.toString());
            System.out.println("Ref: " + refMatchTimes);
            System.out.println();
        }
        
        //The second is just pretty normal integer regex
        myCreationTime = System.currentTimeMillis();
        myPat = new MyPattern("(-?[123456789][0123456789]*|0)");
        System.out.println("My creation time: " + (System.currentTimeMillis() - myCreationTime));
        
        refCreationTime = System.currentTimeMillis();
        refPat = new ReferencePattern("(-?[123456789][0123456789]*|0)");
        System.out.println("ref creation time: " + (System.currentTimeMillis() - refCreationTime));
        String[] testCases = { "0", "123456789", "512", "05121991", "9999999999",
            "-0", "-a123123123123123123", "011235813213465", "21121994a4513455",
            "1", "asdloltrololololo", "1234123412341234789789789789789789789789890",
            "065724", "21316516821491275971", "351684618756176526628712417412751",
            "3151274128461876128631265615165816486184628658165116416486184618625816",
            "2414616516586156a41284681686256g538651865817624861874687561876-517264187",
            "216486256187657126175875976885694875384536856235187168264816581268461864"
                + "124618561826387165612864861284612368126421685129572398472397521"
                + "31658612461876876187568716468736864168461876816581685618658668"
        };
        
        myMatchTimes = 0;
        refMatchTimes = 0;
        myTest = new SpeedTester(myPat);
        refTest = new SpeedTester(refPat);
        
        for(int i = 0; i < testCases.length; i++) {
            System.out.println("Round " + (i+1) + " : ");
            myMatchTimes += myTest.getMatchTime(testCases[i]);
            System.out.println("Mine: " + myMatchTimes);
            refMatchTimes += refTest.getMatchTime(testCases[i]);
            System.out.println("Ref: " + refMatchTimes);
        }
    }
}
