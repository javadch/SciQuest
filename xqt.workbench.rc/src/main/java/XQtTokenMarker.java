


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
        xqtKeywords.add("INTO",Token.KEYWORD1);
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

    private static void addSystemFunctions(){
        xqtKeywords.add("ABS",Token.KEYWORD2);
        xqtKeywords.add("ACOS",Token.KEYWORD2);
        xqtKeywords.add("ASIN",Token.KEYWORD2);
        xqtKeywords.add("ATAN",Token.KEYWORD2);
        xqtKeywords.add("ATN2",Token.KEYWORD2);
        xqtKeywords.add("CEILING",Token.KEYWORD2);
        xqtKeywords.add("CHARINDEX",Token.KEYWORD2);
        xqtKeywords.add("COS",Token.KEYWORD2);
        xqtKeywords.add("COT",Token.KEYWORD2);
        xqtKeywords.add("CURRENT_TIME",Token.KEYWORD2);
        xqtKeywords.add("CURRENT_DATE",Token.KEYWORD2);
        xqtKeywords.add("CURRENT_TIMESTAMP",Token.KEYWORD2);
        xqtKeywords.add("DAY",Token.KEYWORD2);
        xqtKeywords.add("FLOOR",Token.KEYWORD2);
        xqtKeywords.add("ISDATE",Token.KEYWORD2);
        xqtKeywords.add("ISNULL",Token.KEYWORD2);
        xqtKeywords.add("ISNUMERIC",Token.KEYWORD2);
        xqtKeywords.add("LOG",Token.KEYWORD2);
        xqtKeywords.add("LOG10",Token.KEYWORD2);
        xqtKeywords.add("LOWER",Token.KEYWORD2);
        xqtKeywords.add("LTRIM",Token.KEYWORD2);
        xqtKeywords.add("MONTH",Token.KEYWORD2);
        xqtKeywords.add("PI",Token.KEYWORD2);
        xqtKeywords.add("POWER",Token.KEYWORD2);
        xqtKeywords.add("RAND",Token.KEYWORD2);
        xqtKeywords.add("REPLACE",Token.KEYWORD2);
        xqtKeywords.add("RIGHT",Token.KEYWORD2);
        xqtKeywords.add("ROUND",Token.KEYWORD2);
        xqtKeywords.add("RTRIM",Token.KEYWORD2);
        xqtKeywords.add("SIN",Token.KEYWORD2);
        xqtKeywords.add("SQRT",Token.KEYWORD2);
        xqtKeywords.add("SQUARE",Token.KEYWORD2);
        xqtKeywords.add("STR",Token.KEYWORD2);
        xqtKeywords.add("SUBSTRING",Token.KEYWORD2);
        xqtKeywords.add("TAN",Token.KEYWORD2);
        xqtKeywords.add("UPPER",Token.KEYWORD2);
        xqtKeywords.add("YEAR",Token.KEYWORD2);
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
        xqtKeywords.add("OUTER",Token.KEYWORD1);
    }

    private static void addLiterals() {
    }
}
