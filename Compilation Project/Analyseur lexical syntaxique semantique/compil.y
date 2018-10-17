%{
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include"ListesChainées.h"
#include"quad.h"
#include"Lifo.h"
extern FILE* yyin;
extern char * yytext ;
extern int yyleng;
extern int c;
extern int l;
char* cond;
Lst *fin=NULL;
Lst *init1=NULL;
Lst *init2=NULL;
Lst *instr=NULL;
Lst *expr=NULL;
Element *TS=NULL;
ElementQuad *QU=NULL;
int type,nat;
int yylex();
int yyparse();
int yyerror(char *msg);
char valeur[254]={};
char temp[254]={};
char deb_init[254]={};
char deb_expr[254]={};
char deb_init2[254]={};
int QC=1;
int sauv_BRinstr;
int cpt=1;
int not=0,not2=0;
int indice=0;
char temp2[254]={};
%}


%union 
{char *chaine;
int entier;
float reel;

struct{
int type;
char* val;
} structure;

struct{
char* op1;
char* opr;
char* op2;

int utilise;
int thenelse;
} strcdt;
}

%token  INT FLOAT EQUAL SUPP INF SOE IOE DIFF REPEAT ENDREPEAT DEFINE CHECK ENDCHECK   '+' '-' '*' '/' '&' '|' ';' '(' ')' '{' '}'  
%token <chaine> ID
%token <entier> VE 
%type <entier> Unedec
%token <reel> VR
%type <structure> T
%type <structure> TYPE2
%type <structure> EXPART
%type <structure> E
%type <structure> F
%type <structure> TYPE
%type <structure> VALEUR
%type <strcdt> EXPRESSION
%type <strcdt> B
%type <strcdt> C
%type <strcdt> A
%type <strcdt> UNEEXP
%type <chaine> CMP
%left '|'
%left '&'
%%
S : ID '{' CORPS '}'     {printf ("Le programme a ete recconnu \n"); return 0;}
;
CORPS : DEC '{' INSTR '}';

DEC: Unedec 
|DEC Unedec ;
Unedec : DVAR  {$$=1;}
|DCONST {$$=2;};

DVAR : T LID ';'   
T: INT {type=1;}
| FLOAT{type=2;};
LID : ID {if (!RechercheElt(TS,$1)) InsertElt(&TS,$1, type, 1);else {printf("%d:%d: %s:  Declaree deja!!\n",c,l,yytext);exit(1);}}
| LID ',' ID{if (!RechercheElt(TS,$3))InsertElt(&TS,$3, type, 1);else {printf("%d:%d: %s:  Declaree deja!!\n",c,l,yytext-yyleng);exit(1);}};

DCONST: DEFINE  T LLID;
LLID : ID '=' TYPE ';' {if (!RechercheElt(TS,$1))InsertElt(&TS,$1, type, 2);else {printf("%d:%d: %s:  Declaree deja!!\n",c,l,yytext-yyleng);
exit(1);}};

INSTR :  INSTR  UNEISTR | UNEISTR ;
UNEISTR : CONDITION  | AFFECT  | BOUCLE;

CONDITION :  W INSTR ENDCHECK {sprintf(temp,"else%d",indice);MettrejQ(QU,temp,QC);indice--;} //mettre ajour else (fin )
|Z INSTR ENDCHECK {sprintf(temp,"fin%d",indice);MettrejQ(QU,temp,QC);indice--;};//metre a jour fin
Z:W INSTR ':'{sprintf(temp2,"fin%d",indice);InsertQuad(&QU,QC,"BR",temp2," "," ");QC++;printf("%d",indice);sprintf(temp,"else%d",indice);MettrejQ(QU,temp,QC);};//generer un branchement vers la fin et mettre a jour les else
W: V ':'{sprintf(temp,"then%d",indice);MettrejQ(QU,temp,QC);};//metre a jour les then
V: CHECK {indice++;}'(' EXPRESSION ')' {if ($4.utilise==0) {sprintf(temp2,"else%d",indice);if(not2==0)InsertQuad(&QU,QC,$4.opr,temp2,$4.op1,$4.op2);
                                        else {sprintf(temp2,"else%d",indice);InsertQuad(&QU,QC,inverser($4.opr),temp2,$4.op1,$4.op2);} QC++;}not2=0;} ;

EXPRESSION : EXPRESSION '|'  B {if ($1.utilise==0) {if (not==0){sprintf(temp2,"then%d",indice);InsertQuad(&QU,QC,inverser($1.opr),temp2,$1.op1,$1.op2);}
                           else {sprintf(temp2,"else%d",indice);InsertQuad(&QU,QC,inverser($1.opr),temp2,$1.op1,$1.op2);} QC++;$$.utilise=1;}
      if ($3.utilise==0){if (not==0) {sprintf(temp2,"else%d",indice);InsertQuad(&QU,QC,$3.opr,temp2,$3.op1,$3.op2);}
                         else {sprintf(temp2,"else%d",indice);InsertQuad(&QU,QC,inverser($3.opr),temp2,$3.op1,$3.op2);} QC++;$3.utilise=1;}}

| B {$$.op1=strdup($1.op1);$$.op2=strdup($1.op2);$$.utilise=$1.utilise;$$.opr=strdup($1.opr);}
;
B : B '&' C {if ($1.utilise==0){if(not==0){sprintf(temp2,"else%d",indice);InsertQuad(&QU,QC,$1.opr,temp2,$1.op1,$1.op2);}
                       else {sprintf(temp2,"then%d",indice);InsertQuad(&QU,QC,$1.opr,temp2,$1.op1,$1.op2);}  $$.utilise=1;QC++;}
if ($3.utilise==0){if (not==0){sprintf(temp2,"else%d",indice);InsertQuad(&QU,QC,$3.opr,temp2,$3.op1,$3.op2);}
                   else {sprintf(temp2,"else%d",indice);InsertQuad(&QU,QC,inverser($3.opr),temp2,$3.op1,$3.op2);} QC++;$3.utilise=1;}}
|C {$$.op1=strdup($1.op1);$$.op2=strdup($1.op2);$$.utilise = $1.utilise;$$.opr=strdup($1.opr);};

C : A ')'{not=0;$$.op1=strdup($1.op1);$$.op2=strdup($1.op2);$$.utilise = $1.utilise; $$.opr=strdup($1.opr);}
| '(' EXPRESSION ')' {$$.op1=strdup($2.op1);$$.op2=strdup($2.op2);$$.utilise = $2.utilise;$$.opr=strdup($2.opr);}
| UNEEXP {$$.op1=strdup($1.op1);$$.op2=strdup($1.op2);$$.utilise=$1.utilise;$$.opr=strdup($1.opr);}; 
A:'!' {not=1;not2=1;}'(' EXPRESSION {$$.op1=strdup($4.op1);$$.op2=strdup($4.op2);$$.utilise = $4.utilise; $$.opr=strdup($4.opr);};

UNEEXP : TYPE CMP TYPE {if ($1.type!=$3.type) {printf("%d:%d:  incompatibilite de type\n",c,l);exit(1);}else {$$.op1=strdup($1.val);$$.op2=strdup($3.val);$$.utilise=0;$$.opr=strdup($2);}};

TYPE : EXPART {$$.type=$1.type;$$.val=strdup($1.val);};

EXPART : EXPART '+' E {if ($1.type!=$3.type) {printf("%d:%d:  incompatibile de type\n",c,l);exit(1);} else{ $$.type=$1.type;				sprintf(temp,"T%d",cpt);cpt++;InsertQuad(&QU,QC,"+",$1.val,$3.val,temp);QC++;$$.val=strdup(temp);}}
| EXPART '-' E {if ($1.type!=$3.type) {printf("%d:%d:  incompatibile de type\n",c,l);exit(1);} else {$$.type=$1.type;sprintf(temp,"T%d",cpt);cpt++;InsertQuad(&QU,QC,"-",$1.val,$3.val,temp);QC++;$$.val=strdup(temp);}}
| E {$$.type=$1.type;  $$.val=strdup($1.val);};
E : E '*' F {if ($1.type!=$3.type) {printf("%d:%d:  incompatibile de type\n",c,l);exit(1);} else {$$.type=$1.type;sprintf(temp,"T%d",cpt);cpt++;InsertQuad(&QU,QC,"*",$1.val,$3.val,temp);QC++;$$.val=strdup(temp);}}
| E '/' F {if ($1.type!=$3.type) {printf("%d:%d:  incompatibile de type\n",c,l);exit(1);} else {$$.type=$1.type;sprintf(temp,"T%d",cpt);cpt++;InsertQuad(&QU,QC,"/",$1.val,$3.val,temp);QC++;$$.val=strdup(temp);}}
| F {$$.type=$1.type;$$.val=strdup($1.val);};

F : '(' EXPART ')'{$$.type=$2.type;$$.val=strdup($2.val);}
   | TYPE2   {$$.type=$1.type;$$.val=strdup($1.val);};




AFFECT : ID {if (!RechercheElt(TS,$1)){printf ("%d:%d: %s:  Non declaree\n",c,l,yytext-yyleng);exit(1);}else if(getNature(TS,$1)==2){ printf ("%d:%d: %s:  Constante inchangeable\n",c,l,yytext-yyleng);exit(1);}}
'=' TYPE ';'  {if (getType(TS,$1)!=$4.type) {printf("%d:%d:  incompatibile de type\n",c,l);exit(1);}else {InsertQuad(&QU,QC,"=",$4.val," ",$1);QC++;}};

INIT : ID {if (!RechercheElt(TS,$1)){printf ("%d:%d: %s:  Non declaree\n",c,l,yytext-yyleng);exit(1);}else if(getNature(TS,$1)==2){ printf ("%d:%d: %s:  Constante inchangeable\n",c,l,yytext-yyleng);exit(1);}}
'=' TYPE  {if (getType(TS,$1)!=$4.type){printf("%d:%d:  incompatibile de type\n",c,l);exit(1);}else  {InsertQuad(&QU,QC,"=",$4.val," ",$1);QC++;}};
CMP : EQUAL {$$=strdup("BNE");}
| SUPP {$$=strdup("BLE");}
| INF {$$=strdup("BGE");}
| SOE {$$=strdup("BL");}
| IOE {$$=strdup("BG");}
| DIFF {$$=strdup("BE");} ;


BOUCLE :BOUCLE5 INSTR ENDREPEAT {sprintf(temp,"%d",getElt(&init2));InsertQuad(&QU,QC,"BR",temp," "," ");QC++;MajQuad(QU,getElt(&fin),QC);};  
BOUCLE5: BOUCLE4 ':'{MajQuad(QU,sauv_BRinstr,QC);} ;

BOUCLE4: BOUCLE3 ':' INIT {InsertQuad(&QU,QC,"BR",deb_expr," "," ");QC++;};

BOUCLE3: BOUCLE2 '(' UNEEXP')' {InsertLifo(&fin,QC);InsertQuad(&QU,QC,$3.opr,"fin",$3.op1,$3.op2);QC++;sauv_BRinstr=QC;InsertQuad(&QU,QC,"BR","inste"," "," ");QC++; InsertLifo(&init2,QC);};

BOUCLE2 : REPEAT ':' INIT ':' {sprintf(deb_expr,"%d",QC);};



TYPE2 : ID {if (!RechercheElt(TS,$1)){printf ("%d:%d: %s:  Non declaree\n",c,l,yytext-yyleng);exit(1);}else{ $$.type=getType(TS,$1);$$.val=strdup($1);}}
| VALEUR{$$.type=$1.type;$$.val=strdup($1.val);}; 
VALEUR : VR {$$.type=2;sprintf(valeur,"%f",$1); $$.val=strdup(valeur);}
| VE{$$.type=1;sprintf(valeur,"%d",$1); $$.val=strdup(valeur);}

%%
int yyerror(char* msg)
{printf("%s\n",msg);

return 1;
}
int main()
{
yyin=fopen("code.txt","r");
yyparse();
AffichageElts(TS);
AffichageQuad(QU);
return 0;
}
