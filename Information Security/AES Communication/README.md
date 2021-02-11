# Tema 1
## Compilare  
Pentru a compila sursele, se rulează următoarele comenzi:
 - gcc KM.c helper.c aes.c -o KM
 - gcc A.c helper.c aes.c -o A
 - gcc B.c helper.c aes.c -o B
 
 
 
 ## Rulare  
 Pentru a rula executabilele, se pot introduce următoarele exemple de comenzi, în această ordine:
 - ./KM 2800 abcdefghij123456
 - ./A 127.0.0.1 2800 2900 64 abcdefghij123456 aes_A.txt
 - ./B 127.0.0.1 2900 64 abcdefghij123456 aes_B.txt
 
 Pentru nodul Key Manager, parametrii comenzii de rulare a executabilului sunt următorii, în această ordine:
 - [port]: portul la care se deschide socket-ul server-ului Key Manager
 - [K3]: cheia cu care se decriptează modul de operare (CBC sau OFB) şi se criptează cheia corespunzătoare lui

 Pentru nodul A, parametrii comenzii de rulare a executabilului sunt următorii, în această ordine:
 - [Key Manager adress]: adresa server-ului Key Manager
 - [Key Manager port]: portul de conectare la server-ul Key Manager
 - [A port]: portul la care se deschide socket-ul server-ului A
 - [q]: numărul de blocuri care se criptează cu o cheie, ca mai apoi sa se transmită o nouă cheie
 - [K3]: cheia cu care se criptează şi se decriptează modul de operare (CBC sau OFB), cheia corespunzătoare lui şi vectorul de iniţializare
 - [file name]: fişierul care va fi criptat şi trimis, bloc cu bloc
 
 Pentru nodul B, parametrii comenzii de rulare a executabilului sunt următorii, în această ordine:
 - [A adress]: adresa server-ului A
 - [A port]: portul de conectare la server-ul A
 - [q]: numărul de blocuri care se decriptează cu o cheie, ca mai apoi sa se transmită o nouă cheie
 - [K3]: cheia cu care se decriptează modul de operare (CBC sau OFB), cheia corespunzătoare lui şi vectorul de iniţializare
 - [file name]: fişierul în care se vor scrie, pe rând, blocurile primite şi decriptate
  
  
  
## Implementare  
Nodul Key Manager este un server la care se conectează nodul A.  
Nodul A este un server la care se conectează nodul B.  
Nodul B este un client care se conectează la nodul A.  
Pentru criptarea unui bloc se foloseşte implementarea AES-ului în modul ECB, pe 128 de biţi, regăsită în repository-ul https://github.com/kokke/tiny-AES-c.  
Dacă ultimul bloc de partajat nu are 128 de biţi, se adaugă pe fiecare octet rămas numărul de octeţi care se completează, urmând, la decriptare, să fie eliminaţi.  
După ce se trimite tot fişierul, comunicarea dintre A şi B se încheie, ca mai apoi să se poată conecta un alt nod B la A.  
Dacă, la o reiniţializare a cheii, se introduce de la tastatură un şir de caractere gol ca mod de operare, întreaga comunicare se finalizează.
