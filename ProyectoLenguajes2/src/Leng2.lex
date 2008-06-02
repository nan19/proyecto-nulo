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

LETRA=[A-Za-z]
DIGITO=[0-9]
ALFA_NUMERICO={LETRA}|{DIGITO}
IDENT=[a-z]({ALFA_NUMERICO}|_)*
ENTERO=({DIGITO})+
REAL=[0-9]+[.][0-9]+
ESPACIO=([\ \n\r\t\f])+

%%

<YYINITIAL> {ESPACIO} { }

<YYINITIAL> {ENTERO} { 
  return new Symbol(sym.ENTERO, new Integer(yytext()));
}
<YYINITIAL> {REAL} { 
  return new Symbol(sym.REAL, new Float(yytext()));
}

<YYINITIAL> "proc" { 
  return new Symbol(sym.PROC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "finp" { 
  return new Symbol(sym.FINPROC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "tipo" { 
  return new Symbol(sym.TIPODATO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "fint" { 
  return new Symbol(sym.FINT, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "func" { 
  return new Symbol(sym.FUNC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "finf" { 
  return new Symbol(sym.FINFUNC, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> ":" { 
  return new Symbol(sym.DOSPUNTOS, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "e" { 
  return new Symbol(sym.ENTRADA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "es" { 
  return new Symbol(sym.ENTSAL, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "s" { 
  return new Symbol(sym.SALIDA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
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
<YYINITIAL> "registro" { 
  return new Symbol(sym.REGISTRO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "finr" { 
  return new Symbol(sym.FINREG, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "caso" { 
  return new Symbol(sym.CASO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "es" { 
  return new Symbol(sym.ES, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> "[" { 
  return new Symbol(sym.ACORCH, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "]" { 
  return new Symbol(sym.CCORCH, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "," { 
  return new Symbol(sym.COMA, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}
<YYINITIAL> "." { 
  return new Symbol(sym.PUNTO, new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
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
<YYINITIAL> "redondeo" { 
  return new Symbol(sym.REDONDEO, new TokenValue(yytext(), yyline, yychar, sourceFilename));
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
  return new Symbol(sym.ID,new TokenValue(yytext(), yyline, yychar, sourceFilename)); 
}

<YYINITIAL> . {
  return new Symbol(sym.error, null);
}