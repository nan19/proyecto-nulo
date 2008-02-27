import java_cup.runtime.*;
/**
 * Universidad Simon Bolivar
 * Lenguajes de Programacion II
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
		51,
		53
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
		/* 47 */ YY_NOT_ACCEPT,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT,
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
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
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
		/* 106 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"36:9,37,1,36,37,38,36:18,37,30,36:4,28,36,18,19,22,20,36,21,3,23,2:10,34,35" +
",33,31,32,36:2,40:26,36:4,41,36,14,16,4,24,8,11,39,26,7,39:2,15,6,9,5,27,39" +
",13,17,12,39,25,39:3,10,36,29,36:3,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,107,
"0,1,2,3,1,4,1:5,5,1:2,6,1,7,8,1,9,1:6,10,11,12,11:14,1:4,10,13,14,15,16,17," +
"18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42," +
"43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,11,58,59,60,61,62,63,64,65,11," +
"66,67,68,69")[0];

	private int yy_nxt[][] = unpackFromString(70,42,
"1,2,3,4,5,102,67,102,104,102:2,68,90,80,102:2,105,49,6,7,8,9,10,11,69,102,9" +
"1,81,12,13,14,15,16,17,48,18,4,2:2,102,4:2,-1:43,2,-1:35,2:2,-1:5,3,47,-1:4" +
"0,102,-1,102,106,102,92,102:10,-1:6,102:4,-1:11,102:2,93,-1:22,20,21,-1:49," +
"22,-1:41,23,-1:41,24,-1:12,102,-1,102:5,57,102:8,-1:6,102:4,-1:11,102:2,93," +
"-1:2,26,-1:41,102,-1,102:14,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:13,30" +
",-1:6,102:2,31,102,-1:11,102:2,93,-1:31,25,-1:12,102,-1,102:3,19,102:10,-1:" +
"6,102:4,-1:11,102:2,93,-1:23,44,-1:18,1,-1,43:20,50,43:15,-1,43:3,-1:2,102," +
"-1,102:14,-1:6,27,102:3,-1:11,102:2,93,1,45,46:40,-1:2,102,-1,102:5,28,102:" +
"8,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:14,-1:6,102,29,102:2,-1:11,102:" +
"2,93,-1:2,102,-1,102:11,32,102:2,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102," +
"33,102:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102,34,102:12,-1:6,102:4,-1" +
":11,102:2,93,-1:2,102,-1,102,35,102:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-" +
"1,102,36,102:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:9,37,102:4,-1:6,1" +
"02:4,-1:11,102:2,93,-1:2,102,-1,102,38,102:12,-1:6,102:4,-1:11,102:2,93,-1:" +
"2,102,-1,102,39,102:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102,40,102:12," +
"-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102,41,102:12,-1:6,102:4,-1:11,102:2," +
"93,-1:2,102,-1,102:9,42,102:4,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102,52," +
"102:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:3,54,102:6,82,102:3,-1:6,1" +
"02:4,-1:11,102:2,93,-1:2,102,-1,102:3,55,102:10,-1:6,102:4,-1:11,102:2,93,-" +
"1:2,102,-1,102:10,56,102:3,-1:6,103,102:3,-1:11,102:2,93,-1:2,102,-1,102:13" +
",58,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:13,59,-1:6,102:4,-1:11,102:2," +
"93,-1:2,102,-1,102:14,-1:6,102:2,60,102,-1:11,102:2,93,-1:2,102,-1,102:4,61" +
",102:9,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:8,62,102:5,-1:6,102:4,-1:1" +
"1,102:2,93,-1:2,102,-1,102:9,63,102:4,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1" +
",102:6,64,102:7,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:5,65,102:8,-1:6,1" +
"02:4,-1:11,102:2,93,-1:2,102,-1,102:10,66,102:3,-1:6,102:4,-1:11,102:2,93,-" +
"1:2,102,-1,102:4,70,102:9,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:3,71,10" +
"2:10,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:11,72,102:2,-1:6,102:4,-1:11" +
",102:2,93,-1:2,102,-1,73,102:13,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,74,10" +
"2:13,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:9,75,102:4,-1:6,102:4,-1:11," +
"102:2,93,-1:2,102,-1,102:4,76,102:9,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,1" +
"02:5,77,102:8,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:10,78,102:3,-1:6,10" +
"2:4,-1:11,102:2,93,-1:2,102,-1,102:4,79,102:9,-1:6,102:4,-1:11,102:2,93,-1:" +
"2,102,-1,102:4,83,102:9,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:10,84,102" +
":3,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:4,85,102:9,-1:6,102:4,-1:11,10" +
"2:2,93,-1:2,102,-1,102:8,86,102:5,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102" +
",97,102:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:3,98,102:10,-1:6,102:4" +
",-1:11,102:2,93,-1:2,102,-1,102:11,100,102:2,-1:6,102:4,-1:11,102:2,93,-1:2" +
",102,-1,102:4,87,102:9,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:5,101,102:" +
"8,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:4,88,102:9,-1:6,102:4,-1:11,102" +
":2,93,-1:2,102,-1,102:14,-1:6,89,102:3,-1:11,102:2,93,-1:2,102,-1,102,99,10" +
"2:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,102:5,94,102:8,-1:6,102:4,-1:11," +
"102:2,93,-1:2,102,-1,102,95,102:12,-1:6,102:4,-1:11,102:2,93,-1:2,102,-1,10" +
"2:2,96,102:11,-1:6,102:4,-1:11,102:2,93");

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
  return new Symbol(sym.NUMERO, new Integer(yytext()));
}
					case -4:
						break;
					case 4:
						{
  return new Symbol(sym.error, null);
}
					case -5:
						break;
					case 5:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -6:
						break;
					case 6:
						{ 
  return new Symbol(sym.APARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -7:
						break;
					case 7:
						{ 
  return new Symbol(sym.CPARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -8:
						break;
					case 8:
						{ 
  return new Symbol(sym.MAS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -9:
						break;
					case 9:
						{ 
  return new Symbol(sym.MENOS, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -10:
						break;
					case 10:
						{ 
  return new Symbol(sym.POR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -11:
						break;
					case 11:
						{ 
  return new Symbol(sym.ENTRE, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -12:
						break;
					case 12:
						{ 
  return new Symbol(sym.YLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -13:
						break;
					case 13:
						{ 
  return new Symbol(sym.OLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -14:
						break;
					case 14:
						{ 
  return new Symbol(sym.NOLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -15:
						break;
					case 15:
						{ 
  return new Symbol(sym.IGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -16:
						break;
					case 16:
						{ 
  return new Symbol(sym.MAYOR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -17:
						break;
					case 17:
						{ 
  return new Symbol(sym.MENOR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -18:
						break;
					case 18:
						{ 
  return new Symbol(sym.PYCOMA, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -19:
						break;
					case 19:
						{ 
  return new Symbol(sym.SI, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -20:
						break;
					case 20:
						{
  yybegin(COMMENTS);
}
					case -21:
						break;
					case 21:
						{
  yybegin(COMMENTSLIN);
}
					case -22:
						break;
					case 22:
						{ 
  return new Symbol(sym.DIF, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -23:
						break;
					case 23:
						{ 
  return new Symbol(sym.MAYORIGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -24:
						break;
					case 24:
						{ 
  return new Symbol(sym.MENORIGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -25:
						break;
					case 25:
						{ 
  return new Symbol(sym.ASIGNACION, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -26:
						break;
					case 26:
						{ 
  return new Symbol(sym.REAL, new Float (yytext()));
}
					case -27:
						break;
					case 27:
						{ 
  return new Symbol(sym.MOD, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -28:
						break;
					case 28:
						{ 
  return new Symbol(sym.FIN, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -29:
						break;
					case 29:
						{ 
  return new Symbol(sym.DIV, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -30:
						break;
					case 30:
						{ 
  return new Symbol(sym.FINSI, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -31:
						break;
					case 31:
						{ 
  return new Symbol(sym.FINHACER, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -32:
						break;
					case 32:
						{ 
  return new Symbol(sym.DECLREAL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -33:
						break;
					case 33:
						{ 
  return new Symbol(sym.SINO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -34:
						break;
					case 34:
						{ 
  return new Symbol(sym.PISO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -35:
						break;
					case 35:
						{ 
  return new Symbol(sym.FALSO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -36:
						break;
					case 36:
						{ 
  return new Symbol(sym.TECHO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -37:
						break;
					case 37:
						{ 
  return new Symbol(sym.HACER, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -38:
						break;
					case 38:
						{ 
  return new Symbol(sym.CIERTO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -39:
						break;
					case 39:
						{ 
  return new Symbol(sym.DECLENTERO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -40:
						break;
					case 40:
						{ 
  return new Symbol(sym.COMIENZO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -41:
						break;
					case 41:
						{ 
  return new Symbol(sym.DECLBOOL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
					case -42:
						break;
					case 42:
						{ 
  return new Symbol(sym.REDONDEAR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
}
					case -43:
						break;
					case 43:
						{
}
					case -44:
						break;
					case 44:
						{
  yybegin(YYINITIAL);
}
					case -45:
						break;
					case 45:
						{
  yybegin(YYINITIAL);
}
					case -46:
						break;
					case 46:
						{
}
					case -47:
						break;
					case 48:
						{
  return new Symbol(sym.error, null);
}
					case -48:
						break;
					case 49:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -49:
						break;
					case 50:
						{
}
					case -50:
						break;
					case 52:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -51:
						break;
					case 54:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -52:
						break;
					case 55:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -53:
						break;
					case 56:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -54:
						break;
					case 57:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -55:
						break;
					case 58:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -56:
						break;
					case 59:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -57:
						break;
					case 60:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -58:
						break;
					case 61:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -59:
						break;
					case 62:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -60:
						break;
					case 63:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -61:
						break;
					case 64:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -62:
						break;
					case 65:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -63:
						break;
					case 66:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -64:
						break;
					case 67:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -65:
						break;
					case 68:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -66:
						break;
					case 69:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -67:
						break;
					case 70:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -68:
						break;
					case 71:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -69:
						break;
					case 72:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -70:
						break;
					case 73:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -71:
						break;
					case 74:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -72:
						break;
					case 75:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -73:
						break;
					case 76:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -74:
						break;
					case 77:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -75:
						break;
					case 78:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -76:
						break;
					case 79:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -77:
						break;
					case 80:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -78:
						break;
					case 81:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -79:
						break;
					case 82:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -80:
						break;
					case 83:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -81:
						break;
					case 84:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -82:
						break;
					case 85:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -83:
						break;
					case 86:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -84:
						break;
					case 87:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -85:
						break;
					case 88:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -86:
						break;
					case 89:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -87:
						break;
					case 90:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -88:
						break;
					case 91:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -89:
						break;
					case 92:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -90:
						break;
					case 93:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -91:
						break;
					case 94:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -92:
						break;
					case 95:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -93:
						break;
					case 96:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -94:
						break;
					case 97:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -95:
						break;
					case 98:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -96:
						break;
					case 99:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -97:
						break;
					case 100:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -98:
						break;
					case 101:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -99:
						break;
					case 102:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -100:
						break;
					case 103:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -101:
						break;
					case 104:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -102:
						break;
					case 105:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -103:
						break;
					case 106:
						{ 
  return new Symbol(sym.ID, new String(yytext())); 
}
					case -104:
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
