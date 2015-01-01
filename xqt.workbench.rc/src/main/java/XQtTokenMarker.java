


import com.jidesoft.editor.KeywordMap;
import com.jidesoft.editor.tokenmarker.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jfd
 */
public class XQtTokenMarker extends SQLTokenMarker {

    private static KeywordMap xqtKeywords;
    public XQtTokenMarker(){
            super(getKeywordMap(), false);
    }

    public static KeywordMap getKeywordMap(){
        if (xqtKeywords == null) {
                xqtKeywords = new KeywordMap(true);
                addKeywords();
                addDataTypes();
                addSystemFunctions();
                addOperators();
                addLiterals();
        }
        return xqtKeywords;
    }	

    private static void addKeywords(){
        
        // PERSPECTIVE
        xqtKeywords.add("PERSPECTIVE",Token.KEYWORD1);
        xqtKeywords.add("PERS",Token.KEYWORD1);
        xqtKeywords.add("EXTENDS",Token.KEYWORD1);   
        xqtKeywords.add("MapTo",Token.KEYWORD1);   
        xqtKeywords.add("ATTRIBUTE",Token.KEYWORD1);
        xqtKeywords.add("ATT",Token.KEYWORD1);
        
        // CONNECTION
        xqtKeywords.add("CONNECTION",Token.KEYWORD1);
        xqtKeywords.add("ADAPTER",Token.KEYWORD1);
        xqtKeywords.add("SOURCE_URI",Token.KEYWORD1);
        xqtKeywords.add("PARAMETERS",Token.KEYWORD1);
        
        //BIND
        xqtKeywords.add("BIND",Token.KEYWORD1);

        // SELECT
        xqtKeywords.add("SCOPE",Token.KEYWORD1);
        xqtKeywords.add("LIMIT",Token.KEYWORD1);
        xqtKeywords.add("SKIP",Token.KEYWORD1);
        xqtKeywords.add("TAKE",Token.KEYWORD1);
        xqtKeywords.add("INTO",Token.KEYWORD1);        
        xqtKeywords.add("ASC",Token.KEYWORD1);
        xqtKeywords.add("BY",Token.KEYWORD1);
        xqtKeywords.add("DESC",Token.KEYWORD1);
        xqtKeywords.add("DISTINCT",Token.KEYWORD1);
        xqtKeywords.add("FROM",Token.KEYWORD1);
        xqtKeywords.add("GROUP",Token.KEYWORD1);
        xqtKeywords.add("HAVING",Token.KEYWORD1);
        xqtKeywords.add("ORDER",Token.KEYWORD1);
        xqtKeywords.add("SELECT",Token.KEYWORD1);
        xqtKeywords.add("WHERE",Token.KEYWORD1);
                
        xqtKeywords.add("AS",Token.KEYWORD1);
        xqtKeywords.add("DEFAULT",Token.KEYWORD1);
        xqtKeywords.add("DELETE",Token.KEYWORD1);
        
        xqtKeywords.add("IS",Token.KEYWORD1);
        xqtKeywords.add("MAX",Token.KEYWORD1);
        xqtKeywords.add("MIN",Token.KEYWORD1);
        xqtKeywords.add("OF",Token.KEYWORD1);
        xqtKeywords.add("OFFSETS",Token.KEYWORD1);
        xqtKeywords.add("ON",Token.KEYWORD1);
        xqtKeywords.add("TO",Token.KEYWORD1);
        
    }

    private static void addDataTypes(){
        xqtKeywords.add("boolean",Token.KEYWORD2);
        xqtKeywords.add("bool",Token.KEYWORD2);
        xqtKeywords.add("string",Token.KEYWORD2);
        xqtKeywords.add("str",Token.KEYWORD2);
        xqtKeywords.add("byte",Token.KEYWORD2);
        xqtKeywords.add("int",Token.KEYWORD2);
        xqtKeywords.add("integer",Token.KEYWORD2);
        xqtKeywords.add("long",Token.KEYWORD2);
        xqtKeywords.add("real",Token.KEYWORD2);
        xqtKeywords.add("date",Token.KEYWORD2);            
    }

    // this method should grab all the information from the functions registered in the defalt adapter.
    private static void addSystemFunctions(){
        // MATH functions
        xqtKeywords.add("ABS",Token.KEYWORD2); //Returns the absolute value of a number.
        xqtKeywords.add("ACOS",Token.KEYWORD2);
        xqtKeywords.add("ASIN",Token.KEYWORD2);
        xqtKeywords.add("ATAN",Token.KEYWORD2);
        xqtKeywords.add("ATN2",Token.KEYWORD2);
        xqtKeywords.add("CEILING",Token.KEYWORD2); // Rounds a noninteger value upwards to the next greatest integer. Returns an integer value unchanged.
        xqtKeywords.add("COS",Token.KEYWORD2);
        xqtKeywords.add("EXP",Token.KEYWORD2); // Raises a value to the power of the mathematical constant known as e.
        xqtKeywords.add("FLOOR",Token.KEYWORD2); // Rounds a noninteger value downwards to the next least integer. Returns an integer value unchanged.
        xqtKeywords.add("LOG",Token.KEYWORD2); // Returns the natural logarithm of a number.
        xqtKeywords.add("LOG10",Token.KEYWORD2); // Returns the base 10 logarithm of a number.
        xqtKeywords.add("LOGN",Token.KEYWORD2); // Returns the base N logarithm of a number.
        //xqtKeywords.add("PI",Token.KEYWORD2); // to be defined in the grammar
        xqtKeywords.add("POWER",Token.KEYWORD2); // Raises a number to a specified power.
        xqtKeywords.add("RAND",Token.KEYWORD2);
        xqtKeywords.add("ROUND",Token.KEYWORD2);
        xqtKeywords.add("SIN",Token.KEYWORD2);
        xqtKeywords.add("SQRT",Token.KEYWORD2); // Computes the square root of a number.
        xqtKeywords.add("TAN",Token.KEYWORD2);
        xqtKeywords.add("DEGREEOF",Token.KEYWORD2); // Converts an angle measured in radians to an approximately equivalent angle measured in degrees
        xqtKeywords.add("RADIANSOF",Token.KEYWORD2); // Converts an angle measured in degrees to an approximately equivalent angle measured in radians
        
        // STRING functions
        xqtKeywords.add("INDEXOF",Token.KEYWORD2); // Returns an integer value representing the starting position of a string within the search string.
        xqtKeywords.add("ISDATE",Token.KEYWORD2);
        xqtKeywords.add("ISEMPTY",Token.KEYWORD2);
        xqtKeywords.add("ISNUMERIC",Token.KEYWORD2);
        xqtKeywords.add("LASTINDEXOF",Token.KEYWORD2); // Returns an integer value representing the last starting position of a string within the search string.
        xqtKeywords.add("LENGTH",Token.KEYWORD2); // Returns an integer value representing the number of characters in a string expression.
        xqtKeywords.add("LOWER",Token.KEYWORD2); // Converts a string to all lowercase characters.
        xqtKeywords.add("LTRIM",Token.KEYWORD2); // Removes leading characters from a character string.
        xqtKeywords.add("REPLACE",Token.KEYWORD2); // Replaces all occurrences of a specified string value with another string value.
        xqtKeywords.add("REVERSE",Token.KEYWORD2); // Returns the reverse of a string value.
        xqtKeywords.add("RTRIM",Token.KEYWORD2); // Removes trailing characters from a character string.
        xqtKeywords.add("SUBSTRING",Token.KEYWORD2); // Returns a portion of a string.
        xqtKeywords.add("TRIM",Token.KEYWORD2); // Removes leading and trailing characters from a character string.
        xqtKeywords.add("UPPER",Token.KEYWORD2); // Converts a string to all uppercase characters
                
        // DATE/ TIME
        xqtKeywords.add("DATE",Token.KEYWORD2); // current date
        xqtKeywords.add("DATEOF",Token.KEYWORD2); // date object of the passed string
        xqtKeywords.add("DAY",Token.KEYWORD2); // 
        xqtKeywords.add("HOUR",Token.KEYWORD2); // hour component of a time object
        xqtKeywords.add("ISLEAPYEAR",Token.KEYWORD2);
        xqtKeywords.add("MINUTE",Token.KEYWORD2);
        xqtKeywords.add("MONTH",Token.KEYWORD2);
        xqtKeywords.add("SECOND",Token.KEYWORD2);
        xqtKeywords.add("TIME",Token.KEYWORD2); //current time in the system timezone
        xqtKeywords.add("TIMEOF",Token.KEYWORD2); //time object of the passed string
        xqtKeywords.add("TIMESTAMP",Token.KEYWORD2); // current date and time
        xqtKeywords.add("TIMESTAMP",Token.KEYWORD2); // datetime object of the passed string
        xqtKeywords.add("WEEK",Token.KEYWORD2); 
        xqtKeywords.add("WEEKDAY",Token.KEYWORD2); //Returns the week day name/id of the passed date object.
        xqtKeywords.add("YEAR",Token.KEYWORD2); // year component of a date object

        
        xqtKeywords.add("ISNULL",Token.KEYWORD2);
        //xqtKeywords.add("RIGHT",Token.KEYWORD2);
    }

    private static void addOperators(){           
        xqtKeywords.add("ADD",Token.KEYWORD1);
        xqtKeywords.add("AND",Token.KEYWORD1);
        xqtKeywords.add("OR",Token.KEYWORD1);
        xqtKeywords.add("ALL",Token.KEYWORD1);
        xqtKeywords.add("BETWEEN",Token.KEYWORD1);
        xqtKeywords.add("EXISTS",Token.KEYWORD1);
        xqtKeywords.add("IN",Token.KEYWORD1);
        xqtKeywords.add("INTERSECT",Token.KEYWORD1);
        xqtKeywords.add("JOIN",Token.KEYWORD1);
        xqtKeywords.add("LIKE",Token.KEYWORD1);
        xqtKeywords.add("NOT",Token.KEYWORD1);
        xqtKeywords.add("NULL",Token.KEYWORD1);
        xqtKeywords.add("NUMBER",Token.KEYWORD1);
        xqtKeywords.add("EMPTY",Token.KEYWORD1);
        xqtKeywords.add("INNER",Token.KEYWORD1);
        xqtKeywords.add("OUTER",Token.KEYWORD1);
        xqtKeywords.add("LEFT",Token.KEYWORD1);
        xqtKeywords.add("RIGHT",Token.KEYWORD1);
    }

    private static void addLiterals() {
    }
}
