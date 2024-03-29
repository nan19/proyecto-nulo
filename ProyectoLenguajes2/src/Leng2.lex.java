import java_cup.runtime.*;
/**
 * Universidad Simon Bolivar
 * Lenguajes de Programacion II
 * Entrega final 7/04/2008
 * 
 * Maria Sol Ferrer 04-36975
 * Jamil Navarro 04-37334
 *
 * Proyecto: Procesador de lenguaje imperativo.
 * Leng2.lex: Analizador lexicografico del lenguaje
 * 
 * Este programa esta basado en un ejemplo encontrado en
 * http://bmrc.berkeley.edu/courseware/cs164/fall99/assignment/a1/tutorial.html
 *
 */
/** 
* Clase TokenValue
* Este tipo es el que se creara por cada token
* retornado por el escaner.
* 
*/
class TokenValue {          
  public int lineBegin;		//Linea del programa donde se ubica el token.
  public int charBegin;		//Numero de caracter del token en el programa.
  public String text;		//Contenido del token, expresion reconocida.
  public String filename;  	//Nombre del archivo leido. 
  TokenValue() {
  }
  TokenValue(String text, int lineBegin, int charBegin, String filename) {
    this.text = text; 
    this.lineBegin = lineBegin; 
    this.charBegin = charBegin;
    this.filename = filename;
  }
  public String toString() { 
    return text;
  }
  public boolean toBoolean() {
    return Boolean.valueOf(text).booleanValue();  
  }
  public int toInt() {
    return Integer.valueOf(text).intValue();
  }
}


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

  public String sourceFilename;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int COMMENTSLIN = 2;
	private final int YYINITIAL = 0;
	private final int COMMENTS = 1;
	private final int yy_state_dtrans[] = {
		0,
		67,
		69
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NOT_ACCEPT,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"41:9,42,1,41,42,43,41:18,42,36,41:4,34,41,25,26,29,27,24,28,3,30,2:10,13,40" +
",39,37,38,41:2,45:26,22,41,23,41,46,41,18,20,7,31,14,8,21,33,9,44:2,19,16,1" +
"0,6,4,44,5,15,11,12,32,44:3,17,41,35,41:3,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,133,
"0,1,2,3,1,4,5,6,7,1:8,8,1:2,9,1,10,11,1:3,12,13,1:5,14,15,12:24,1:4,14,16,1" +
"7,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,4" +
"2,12,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,6" +
"6,67,68,69,70,71,72,73,74,75,76,77,12,78,79,80,81")[0];

	private int yy_nxt[][] = unpackFromString(82,47,
"1,2,3,4,5,123,128,129,87,128:2,130,128,6,7,8,88,128:3,131,128,9,10,11,12,13" +
",14,15,16,17,89,128,132,18,19,20,21,22,23,24,25,2:2,128,25:2,-1:48,2,-1:40," +
"2:2,-1:5,3,64,-1:45,128,-1,128,90,128:3,91,128:3,-1,128:8,-1:9,128:3,-1:10," +
"128:2,92,-1:37,26,-1:11,128,-1,128:6,101,128:2,-1,128,27,128:6,-1:9,128:3,-" +
"1:10,128:2,92,-1:2,128,-1,128:5,28,128:3,-1,128:8,-1:9,128:3,-1:10,128:2,92" +
",-1:29,29,30,-1:53,31,-1:46,32,-1:46,33,-1:11,128,-1,128:9,-1,128:8,-1:9,12" +
"8:3,-1:10,128:2,92,-1:2,128,-1,128:6,77,128:2,-1,128:8,-1:9,128:3,-1:10,128" +
":2,92,-1:2,34,-1:46,128,-1,42,43,128:2,44,128:2,45,128,-1,128,46,128:6,-1:9" +
",128:2,47,-1:10,128:2,92,-1:2,128,-1,128:6,35,128:2,-1,128:8,-1:9,128:3,-1:" +
"10,128:2,92,-1:30,61,-1:16,1,-1,60:27,66,60:13,-1,60:3,-1:2,128,-1,128:9,-1" +
",128:8,-1:9,36,128:2,-1:10,128:2,92,1,62,63:45,-1:2,128,-1,128:9,-1,128:8,-" +
"1:9,128,37,128,-1:10,128:2,92,-1:2,128,-1,128:3,38,128:5,-1,128:8,-1:9,128:" +
"3,-1:10,128:2,92,-1:2,128,-1,128:2,39,128:6,-1,128:8,-1:9,128:3,-1:10,128:2" +
",92,-1:2,128,-1,128:9,-1,128:5,40,128:2,-1:9,128:3,-1:10,128:2,92,-1:2,128," +
"-1,128:2,41,128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:3,48,1" +
"28:5,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:2,49,128:6,-1,128:8" +
",-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:2,50,128:6,-1,128:8,-1:9,128:3,-" +
"1:10,128:2,92,-1:2,128,-1,128:2,51,128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92" +
",-1:2,128,-1,128:2,52,128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1," +
"128,53,128:7,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:2,54,128:6," +
"-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:2,55,128:6,-1,128:8,-1:9" +
",128:3,-1:10,128:2,92,-1:2,128,-1,128:2,56,128:6,-1,128:8,-1:9,128:3,-1:10," +
"128:2,92,-1:2,128,-1,128:2,57,128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2" +
",128,-1,128:2,58,128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:2" +
",59,128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:5,65,128:2,97," +
"-1,128:4,98,128:3,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:2,68,128:6,-1,1" +
"28:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:5,70,128:3,-1,128:8,-1:9,128" +
":3,-1:10,128:2,92,-1:2,128,-1,128:2,71,128:6,-1,128:8,-1:9,128:3,-1:10,128:" +
"2,92,-1:2,128,-1,128:9,-1,128,72,128:6,-1:9,128:3,-1:10,128:2,92,-1:2,128,-" +
"1,128:9,-1,128:4,73,128:2,103,-1:9,125,128:2,-1:10,128:2,92,-1:2,128,-1,128" +
":9,-1,128:2,126,128:5,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1,104,12" +
"8:7,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1,128,74,128:6,-1:9,128:3," +
"-1:10,128:2,92,-1:2,128,-1,128:6,75,128:2,-1,128:8,-1:9,128:3,-1:10,128:2,9" +
"2,-1:2,128,-1,128:9,-1,128:5,105,128:2,-1:9,128:3,-1:10,128:2,92,-1:2,128,-" +
"1,76,128:8,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:3,106,128:5,-" +
"1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:7,107,128,-1,128:8,-1:9,1" +
"28:3,-1:10,128:2,92,-1:2,128,-1,128:2,108,128:6,-1,128:8,-1:9,128:3,-1:10,1" +
"28:2,92,-1:2,128,-1,128:5,110,128:3,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2" +
",128,-1,128,112,128:7,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9," +
"-1,128,78,128:6,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1,128:8,-1:9,1" +
"28:2,79,-1:10,128:2,92,-1:2,128,-1,128:9,-1,113,128:7,-1:9,128:3,-1:10,128:" +
"2,92,-1:2,128,-1,128:9,-1,128:5,114,128:2,-1:9,128:3,-1:10,128:2,92,-1:2,12" +
"8,-1,128:9,-1,80,128:7,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1,128,1" +
"15,128:6,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:6,116,128:2,-1,128:8,-1:" +
"9,128:3,-1:10,128:2,92,-1:2,128,-1,128:7,81,128,-1,128:8,-1:9,128:3,-1:10,1" +
"28:2,92,-1:2,128,-1,128,82,128:7,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,12" +
"8,-1,128:9,-1,118,128:7,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:7,119,128" +
",-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1,128:8,-1:9,120,128" +
":2,-1:10,128:2,92,-1:2,128,-1,128:6,121,128:2,-1,128:8,-1:9,128:3,-1:10,128" +
":2,92,-1:2,128,-1,128:9,-1,128:4,122,128:3,-1:9,128:3,-1:10,128:2,92,-1:2,1" +
"28,-1,128,83,128:7,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1," +
"84,128:7,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1,128:3,85,128:4,-1:9" +
",128:3,-1:10,128:2,92,-1:2,128,-1,128:6,86,128:2,-1,128:8,-1:9,128:3,-1:10," +
"128:2,92,-1:2,128,-1,128:9,-1,93,128:7,-1:9,128:3,-1:10,128:2,92,-1:2,128,-" +
"1,128:3,109,128:5,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:2,111," +
"128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:5,127,128:3,-1,128" +
":8,-1:9,128:3,-1:10,128:2,92,-1:2,128,-1,128:9,-1,117,128:7,-1:9,128:3,-1:1" +
"0,128:2,92,-1:2,128,-1,128:2,94,128:2,95,128:3,-1,128:4,96,128:3,-1:9,128:3" +
",-1:10,128:2,92,-1:2,128,-1,128:5,99,128:3,-1,100,128:7,-1:9,128:3,-1:10,12" +
"8:2,92,-1:2,128,-1,128:2,102,128:6,-1,128:8,-1:9,128:3,-1:10,128:2,92,-1:2," +
"128,-1,128:9,-1,128:4,124,128:3,-1:9,128:3,-1:10,128:2,92");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return new Symbol(sym.EOF, null);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ }
					case -3:
						break;
					case 3:
						{ 
  return new Symbol(sym.ENTERO, new Integer(yytext()));
}
					case -4:
						break;
					case 4:
						{ 
  return new Symbol(sym.PUNTO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -5:
						break;
					case 5:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -6:
						break;
					case 6:
						{ 
  return new Symbol(sym.DOSPUNTOS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -7:
						break;
					case 7:
						{ 
  return new Symbol(sym.ENTRADA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -8:
						break;
					case 8:
						{ 
  return new Symbol(sym.SALIDA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -9:
						break;
					case 9:
						{ 
  return new Symbol(sym.ACORCH, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -10:
						break;
					case 10:
						{ 
  return new Symbol(sym.CCORCH, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -11:
						break;
					case 11:
						{ 
  return new Symbol(sym.COMA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -12:
						break;
					case 12:
						{ 
  return new Symbol(sym.APARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -13:
						break;
					case 13:
						{ 
  return new Symbol(sym.CPARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -14:
						break;
					case 14:
						{ 
  return new Symbol(sym.MAS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -15:
						break;
					case 15:
						{ 
  return new Symbol(sym.MENOS, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -16:
						break;
					case 16:
						{ 
  return new Symbol(sym.POR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -17:
						break;
					case 17:
						{ 
  return new Symbol(sym.ENTRE, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -18:
						break;
					case 18:
						{ 
  return new Symbol(sym.YLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -19:
						break;
					case 19:
						{ 
  return new Symbol(sym.OLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -20:
						break;
					case 20:
						{ 
  return new Symbol(sym.NOLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -21:
						break;
					case 21:
						{ 
  return new Symbol(sym.IGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -22:
						break;
					case 22:
						{ 
  return new Symbol(sym.MAYOR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -23:
						break;
					case 23:
						{ 
  return new Symbol(sym.MENOR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -24:
						break;
					case 24:
						{ 
  return new Symbol(sym.PYCOMA, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -25:
						break;
					case 25:
						{
  return new Symbol(sym.error, null);
}
					case -26:
						break;
					case 26:
						{ 
  return new Symbol(sym.ASIGNACION, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -27:
						break;
					case 27:
						{ 
  return new Symbol(sym.ENTSAL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -28:
						break;
					case 28:
						{ 
  return new Symbol(sym.SI, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -29:
						break;
					case 29:
						{
  yybegin(COMMENTS);
}
					case -30:
						break;
					case 30:
						{
  yybegin(COMMENTSLIN);
}
					case -31:
						break;
					case 31:
						{ 
  return new Symbol(sym.DIF, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -32:
						break;
					case 32:
						{ 
  return new Symbol(sym.MAYORIGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -33:
						break;
					case 33:
						{ 
  return new Symbol(sym.MENORIGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -34:
						break;
					case 34:
						{ 
  return new Symbol(sym.REAL, new Float(yytext()));
}
					case -35:
						break;
					case 35:
						{ 
  return new Symbol(sym.FIN, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -36:
						break;
					case 36:
						{ 
  return new Symbol(sym.MOD, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -37:
						break;
					case 37:
						{ 
  return new Symbol(sym.DIV, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -38:
						break;
					case 38:
						{ 
  return new Symbol(sym.PROC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -39:
						break;
					case 39:
						{ 
  return new Symbol(sym.PISO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -40:
						break;
					case 40:
						{ 
  return new Symbol(sym.DECLREAL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -41:
						break;
					case 41:
						{ 
  return new Symbol(sym.CASO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -42:
						break;
					case 42:
						{ 
  return new Symbol(sym.FINPROC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -43:
						break;
					case 43:
						{ 
  return new Symbol(sym.FINREG, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -44:
						break;
					case 44:
						{ 
  return new Symbol(sym.FINFUNC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -45:
						break;
					case 45:
						{ 
  return new Symbol(sym.FINT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -46:
						break;
					case 46:
						{ 
  return new Symbol(sym.FINSI, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -47:
						break;
					case 47:
						{ 
  return new Symbol(sym.FINHACER, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -48:
						break;
					case 48:
						{ 
  return new Symbol(sym.FUNC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -49:
						break;
					case 49:
						{ 
  return new Symbol(sym.TIPODATO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -50:
						break;
					case 50:
						{ 
  return new Symbol(sym.SINO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -51:
						break;
					case 51:
						{ 
  return new Symbol(sym.FALSO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -52:
						break;
					case 52:
						{ 
  return new Symbol(sym.TECHO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -53:
						break;
					case 53:
						{ 
  return new Symbol(sym.HACER, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -54:
						break;
					case 54:
						{ 
  return new Symbol(sym.CIERTO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -55:
						break;
					case 55:
						{ 
  return new Symbol(sym.DECLENTERO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -56:
						break;
					case 56:
						{ 
  return new Symbol(sym.REGISTRO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -57:
						break;
					case 57:
						{ 
  return new Symbol(sym.REDONDEO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -58:
						break;
					case 58:
						{ 
  return new Symbol(sym.COMIENZO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -59:
						break;
					case 59:
						{ 
  return new Symbol(sym.DECLBOOL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -60:
						break;
					case 60:
						{
}
					case -61:
						break;
					case 61:
						{
  yybegin(YYINITIAL);
}
					case -62:
						break;
					case 62:
						{
  yybegin(YYINITIAL);
}
					case -63:
						break;
					case 63:
						{
}
					case -64:
						break;
					case 65:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -65:
						break;
					case 66:
						{
}
					case -66:
						break;
					case 68:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -67:
						break;
					case 70:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -68:
						break;
					case 71:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -69:
						break;
					case 72:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -70:
						break;
					case 73:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -71:
						break;
					case 74:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -72:
						break;
					case 75:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -73:
						break;
					case 76:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -74:
						break;
					case 77:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -75:
						break;
					case 78:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -76:
						break;
					case 79:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -77:
						break;
					case 80:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -78:
						break;
					case 81:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -79:
						break;
					case 82:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -80:
						break;
					case 83:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -81:
						break;
					case 84:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -82:
						break;
					case 85:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -83:
						break;
					case 86:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -84:
						break;
					case 87:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -85:
						break;
					case 88:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -86:
						break;
					case 89:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -87:
						break;
					case 90:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -88:
						break;
					case 91:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -89:
						break;
					case 92:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -90:
						break;
					case 93:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -91:
						break;
					case 94:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -92:
						break;
					case 95:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -93:
						break;
					case 96:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -94:
						break;
					case 97:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -95:
						break;
					case 98:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -96:
						break;
					case 99:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -97:
						break;
					case 100:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -98:
						break;
					case 101:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -99:
						break;
					case 102:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -100:
						break;
					case 103:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -101:
						break;
					case 104:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -102:
						break;
					case 105:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -103:
						break;
					case 106:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -104:
						break;
					case 107:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -105:
						break;
					case 108:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -106:
						break;
					case 109:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -107:
						break;
					case 110:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -108:
						break;
					case 111:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -109:
						break;
					case 112:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -110:
						break;
					case 113:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -111:
						break;
					case 114:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -112:
						break;
					case 115:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -113:
						break;
					case 116:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -114:
						break;
					case 117:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -115:
						break;
					case 118:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -116:
						break;
					case 119:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -117:
						break;
					case 120:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -118:
						break;
					case 121:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -119:
						break;
					case 122:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -120:
						break;
					case 123:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -121:
						break;
					case 124:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -122:
						break;
					case 125:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -123:
						break;
					case 126:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -124:
						break;
					case 127:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -125:
						break;
					case 128:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -126:
						break;
					case 129:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -127:
						break;
					case 130:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -128:
						break;
					case 131:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -129:
						break;
					case 132:
						{ 
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -130:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
