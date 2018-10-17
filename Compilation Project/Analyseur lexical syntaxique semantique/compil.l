%{
  #include <stdio.h>
  #include<stdlib.h>
  #include<string.h>
  #include"compil.tab.h"
  extern YYSTYPE yylval;
  int l=1;
  int c=0;
%}

chiffre [0-9]
entier 0|[1-9]{chiffre}*
idf [a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9]*
reel {chiffre}+"."{chiffre}+
commentaire1 "//"(.)*
commentaire2 ("/*"(([^"*/"])*(\n)*)*"*/")



%%
{commentaire1} {/*printf("le type %s est reconnu\n",yytext);*/ c=c+yyleng; }
{commentaire2} {/*printf("le type %s est reconnu\n",yytext);*/ c=c+yyleng; }
"\n" { /*printf("%s est recconu\n", yytext);*/ l++;c=0;}
"\t" { /*printf("%s est recconu\n", yytext);*/ c=c+8; }
" " {/* printf("%s recognized\n", yytext);*/ c++; }
"," {/* printf("%s recconu\n", yytext);*/ c=c+yyleng; return ',';}
"=" { /*printf("%s reconnu\n", yytext);*/ c=c+yyleng; return '=';}
"-" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; 	return '-';}
"(" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; 	return '(';}
")" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; 	return ')';}
"<=" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; 	return IOE;}
"==" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; 	return EQUAL;}
"!=" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;     return DIFF;}
">=" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; 	return SOE;}
"<" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; return INF;}
">" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; return SUPP;}
"/" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng; return '/';}
"+" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;  return '+';}
"*" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;  return '*';}
"|" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;  return '|';}
"!" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;					return '!';}
"&" {/*printf("%s recognnu\n", yytext);*/	c=c+yyleng;   return '&';}
"{" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;	 return '{';}
"}" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;  return '}';}
"?" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;}
";" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;  return ';';}
":" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;  return ':';}
"_" {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;}
"." {/*printf("%s reconnu\n", yytext);*/	c=c+yyleng;}
"Uint" {/*printf("le type %s est reconnu\n",yytext);*/ c=c+yyleng; return INT;}
"Ufloat" {/*printf("le type %s est reconnu\n",yytext);*/ c=c+yyleng; return FLOAT;}
"define" {/*printf("le mot clé %s est reconnu\n",yytext);*/ c=c+yyleng; return DEFINE;}
"CHECK" {/*printf("le mot clé %s est reconnu\n",yytext);*/ c=c+yyleng; return CHECK;}
"ENDCHECK" {/*printf("le mot clé %s est reconnu\n",yytext);*/ c=c+yyleng; return ENDCHECK;}
"REPEAT" {/*printf("le mot clé %s est reconnu\n",yytext);*/ c=c+yyleng; return REPEAT;}
"ENDREPEAT" {/*printf("le mot clé %s est reconnu\n",yytext);*/ c=c+yyleng; return ENDREPEAT;}

{entier} {if(atoi(yytext)  > 32767) printf("Erreur entier trop grand \n"); else {/* printf("%s reconnun", yytext); */ c=c+yyleng;
yylval.entier=atoi(yytext); return VE;}}
 
{idf} {if(yyleng < 8) {/*printf("%s reconnu\n", yytext);*/
c=c+yyleng;
yylval.chaine=strdup(yytext); 

//printf("%s",yylval.chaine);

return ID;}
else printf("trop long");c=c+yyleng;}

{reel} {/*printf("%s reconnu\n", yytext);*/c=c+yyleng; yylval.reel=atof(yytext); return VR;}


. {printf("\n Lexical error: %s not reconnu\nligne : %d , column : %d\n",yytext,l,c);}
%%

int yywrap(void)
{
return 0;
}
