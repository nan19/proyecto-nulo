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

%%

%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol

%eofval{
  return new Symbol(sym.EOF, null);
%eofval}

%{
  public String sourceFilename;
%}

%line
%char
%state COMMENTS
%state COMMENTSLIN

ALPHA=[A-Za-z_]
DIGIT=[0-9]
ALPHA_NUMERIC={ALPHA}|{DIGIT}
IDENT=[a-z]({ALPHA_NUMERIC}|_)*
NUMBER=({DIGIT})+
REAL=[0-9]+[.][0-9]+
WHITE_SPACE=([\ \n\r\t\f])+

%%

<YYINITIAL> {WHITE_SPACE} { }

<YYINITIAL> {NUMBER} { 
  return new Symbol(sym.NUMERO, new Integer(yytext()));
}
<YYINITIAL> {REAL} { 
  return new Symbol(sym.REAL, new Float (yytext()));
}

<YYINITIAL> "comienzo" { 
  return new Symbol(sym.COMIENZO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "fin" { 
  return new Symbol(sym.FIN, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "entero" { 
  return new Symbol(sym.DECLENTERO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "real" { 
  return new Symbol(sym.DECLREAL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "booleano" { 
  return new Symbol(sym.DECLBOOL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "cierto" { 
  return new Symbol(sym.CIERTO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "falso" { 
  return new Symbol(sym.FALSO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "(" { 
  return new Symbol(sym.APARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}<YYINITIAL> ")" { 
  return new Symbol(sym.CPARENT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "+" { 
  return new Symbol(sym.MAS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
} 
<YYINITIAL> "-" { 
  return new Symbol(sym.MENOS, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "*" { 
  return new Symbol(sym.POR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "/" { 
  return new Symbol(sym.ENTRE, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "div" { 
  return new Symbol(sym.DIV, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "mod" { 
  return new Symbol(sym.MOD, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "techo" { 
  return new Symbol(sym.TECHO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "piso" { 
  return new Symbol(sym.PISO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "redondear" { 
  return new Symbol(sym.REDONDEAR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "&" { 
  return new Symbol(sym.YLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "|" { 
  return new Symbol(sym.OLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "!" { 
  return new Symbol(sym.NOLOG, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "=" { 
  return new Symbol(sym.IGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "!=" { 
  return new Symbol(sym.DIF, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> ">" { 
  return new Symbol(sym.MAYOR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "<" { 
  return new Symbol(sym.MENOR, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> ">=" { 
  return new Symbol(sym.MAYORIGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "<=" { 
  return new Symbol(sym.MENORIGUAL, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 

<YYINITIAL> ":=" { 
  return new Symbol(sym.ASIGNACION, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "si" { 
  return new Symbol(sym.SI, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "sino" { 
  return new Symbol(sym.SINO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "fins" { 
  return new Symbol(sym.FINSI, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "hacer" { 
  return new Symbol(sym.HACER, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "finh" { 
  return new Symbol(sym.FINHACER, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 

<YYINITIAL> ";" { 
  return new Symbol(sym.PYCOMA, new TokenValue(yytext(), yyline, yychar, sourceFilename));
} 
<YYINITIAL> "/*"  {
  yybegin(COMMENTS);
}
<COMMENTS> "*/" {
  yybegin(YYINITIAL);
}
<COMMENTS> . {
}
<YYINITIAL> "//" {
  yybegin(COMMENTSLIN);
}
<COMMENTSLIN> [^\n] {
}
<COMMENTSLIN> [\n] {
  yybegin(YYINITIAL);
}
<YYINITIAL> {IDENT} { 
  return new Symbol(sym.ID, new String(yytext())); 
}

<YYINITIAL> . {
  return new Symbol(sym.error, null);
}