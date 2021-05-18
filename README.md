# ProjecteRMI
Projecte de RMI (Remote Method Implementation), per a Computació Distribuïda i Aplicacions en el Grau d'Enginyeria Informàtica

## Executar
Per fer una prova del codi s'ha de descarregar les dues carpetes i executar-les per separat primer el servidor perquè el client es pugui connectar amb el servidor. 
### Servidor:

Dins de la carpeta es troba un examen d'exemple de Projecte Web, a l'iniciar el servidor pregunta sobre el nom de fitxer, si és el cas de què es vol provar amb l'examen que hi ha, s'haurà d'introduir el nom "Exam.csv", si es vol introduir un nou examen ha d'estar dins de la carpeta del servidor, amb el format ha de ser el següent:

--> **Question?;choice1;choice2;choice3;…;correct_answer_number**

Després en finalitzar es sobreescriurà el fitxer "Notes_Examen.txt", en el cas que no estigui es crearia un fitxer amb aquest nom.
### Client:
A l'iniciar el client es demanarà un id, el id pot ser des d'un DNI a un número.  

## Fet per:

- Ahmed Azzamouri Al-lal
- Iñaki Serrano Navarro
