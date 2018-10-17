#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef  struct Lst {
	int Num;	
	struct Lst* Suivant;
			} Lst;

void InsertLifo(Lst** List,int num)	{

Lst* nouveau = malloc(sizeof(Lst));

nouveau->Num = num;

nouveau->Suivant = *List;
*List=nouveau;

						}

int getElt(Lst** tete){
Lst *t=*tete;
int n;

n=t->Num;
t=t->Suivant;
*tete=t;
return n;
}
