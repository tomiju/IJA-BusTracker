# IJA - Projekt 2020
### Hodnocení: 75/80
"Bus tracker - Aplikace pro zobrazeni linek hromadne dopravy a sledovani jejich pohybu"
* Run with **ant**:   
`ant compile`  
`ant run`
____________________________
### Autori
* Tomas Julina (xjulin08)
*	Tomas Kantor (xkant14)

### Poznamky:
* Prelozitelne na Merlin
* Prelozitelne na Windows 10 (testovana i funkcionalita)
*	Prelozitelne na Ubuntu 18.04 (testovana i funkcionalita)

### Popis:
* Spusteni a ovladani:
Po spusteni .jar souboru se zobrazi uvodni okno, vyberete soubor se vstupnimi daty a pote se vykresli
mapa a simulace zacne se zakladni rychlosti - je mozne menit rychlost simulace - rychlost 1 az 5 - po
zmene rychlosti je nutne tuto zmenu potvrdit pomoci klavesy "Enter" nebo tlacitka. Je mozne zvyraznit
trasu jednotlivych linek i vozidel, ktere temto linkam patri - kliknuti na nazev linky v seznamu v leve
casti - zaroven se zobrazi seznam ulic, ze kterych se linka sklada.

* Vozidla v zakladnim modu (bez editace) i v modu s editovanou trasou dodrzuji jizdni rad a zastavky
(vse definovane ve vstupnim souboru, v pripade editovane trasy jede spoj pouze na zastavky, ktere
jsou na nove trase).

* Pri stisknuti vozidla (pohybujiciho se na mape) se vozidlo a jeho trasa zvyrazni a zaroven se zobrazi
informace o danem vozidle - cely jeho itinerar s jmeny zastavek a casy - zvyraznena zastavka je ta prave nasledujici.

* Zoom je mozny pomoci stisknuti klavesy CTRL a nasledneho scrollovani koleckem mysi.

* Po kliknuti na "Edit Mode" se aplikace prepne do editacniho modu, v tomto modu je mozne editovat trasu pro
jednotlive linky - je nutne vybrat linku ze seznamu, pote v seznamu "Streets" kliknutim na nazev ulice dojde
k jeji uzavreni. Pred a po uzavreni jakekoliv ulice se ukaze varovani, kterych linek se uzavirka dotkla a je nutne
pro VSECHNY linky, kterym tato ulice patrila vybrat NOVOU TRASU
(kompletní - pokud nektere vozidlo nebude lezet na nove trase, tak nepojede; jednotlive pro kazdou linku) - vyber trasy
probiha nasledovne - jiz pri uzavirani mate vybranou linku, naslednym klikanim na ulice
(POZOR! Je dulezite klikat v poradi od zacatku linky (aby na trase lezela vozidla)! - pokud tak neucinite nebo uzavrete ulici, ktera
ma pouze jednu moznou trasu, vozidla se muzou chovat neocekavane) se do seznamu "New Path" bude pridavat
nova trasa dane linky (v poradi klikani, opetovnym kliknutim se ulice odstrani ze seznamu) - v pripade, ze jste editovali
spravne bude nyni mozne editacni mod opustit (jinak se zobrazi upozorneni) a vozidla pojedou po jejich nove trase - pokud
v editovane trase nejsou zadne zastavky z puvodni cesty, tak vozidlo nema kam jet, musi tam byt alespon posledni zastavka - vozidla budou zastavovat na zastavkach. Delka nove editovane trasy je puvodni cas + nahodne zpozdeni (vypada to realneji).
