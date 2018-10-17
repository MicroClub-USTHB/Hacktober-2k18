

#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef  struct ElementQuad {
        int Num;
	char* prem;
	char* sec;
	char* tri;
        char* rebaa;
	struct ElementQuad* Suivant;
			} ElementQuad;

void InsertQuad(ElementQuad** ListQuad, int numero,char* a1, char* a2, char* a3,char* a4)	{

ElementQuad* nouveau = malloc(sizeof(ElementQuad));

ElementQuad* tete = *ListQuad;

ElementQuad* prec;

nouveau->Num = numero;

nouveau->prem = strdup(a1);

nouveau->sec = strdup(a2);

nouveau->tri = strdup(a3);

nouveau->rebaa = strdup(a4);

nouveau->Suivant = NULL;

if (tete == NULL ) *ListQuad = nouveau ;
else {	while (tete != NULL) {
	prec = tete;
	tete = tete->Suivant;
				}
	prec->Suivant = nouveau;

      }
						}

void MajQuad(ElementQuad* ListQuad , int numero,int nv)	{

ElementQuad* tete = ListQuad;
char c[254]={};
		while(tete != NULL)	{
		if (tete->Num==numero){ sprintf(c,"%d",nv) ;tete->sec=strdup(c);break;}
		else tete = tete->Suivant;
					}
		
	
							}

void MajQuadInv(ElementQuad* ListQuad , int numero)	{

ElementQuad* tete = ListQuad;

char *T;

            while(tete != NULL)	{
            
		if (tete->Num==numero) { break;}
		 tete = tete->Suivant;
				printf("%d",tete->Num);	}

		T = strdup(tete->tri);

		tete->tri=strdup(tete->rebaa);

		tete->rebaa=strdup(T);
	
							}

void AffichageQuad(ElementQuad* ListQuad) 		{


ElementQuad* tete = ListQuad;

	if (tete == NULL) printf("La liste est vide .\n");
	else {  while(tete != NULL)     {
		printf(" %d :  ( %s  |    %s |   %s   | %s )\n",tete->Num,tete->prem,tete->sec,tete->tri,tete->rebaa);
		tete = tete->Suivant ;	}
	     }
						}

char* inverser(char* ch){
char* t;

if(!strcmp(ch,"BE") ) t=strdup("BNE");
else if(!strcmp(ch,"BNE")) t=strdup("BE");
else if(!strcmp(ch,"BL")) t=strdup("BGE");
else if(!strcmp(ch,"BGE")) t=strdup("BL");
else if(!strcmp(ch,"BLE")) t=strdup("BG");
else if(!strcmp(ch,"BG")) t=strdup("BLE");


return t;

}

void MettrejQ(ElementQuad* ListQuad,char* indice,int num) 		{

char val[254]={};
ElementQuad* tete = ListQuad;
   sprintf(val,"%d",num);
	if (tete == NULL) printf("La liste est vide .\n");
	else {  while(tete != NULL)     {
		if(!strcmp(tete->sec,indice)) tete->sec=strdup(val);
		tete = tete->Suivant ;	}
	     }
						}

