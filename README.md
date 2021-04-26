# DeathBan

  Jest to prosty plugin który wykonuje jakąś komende po śmierci
  
# Permisje
  `deathban.bypass` - permisja dająca ochronę przed działaniem komendy po śmierci
## Co dodaliśmy nowego?
V1.5.3
  * Dodaliśmy fukcje 'Time' w pliku **config.yml**, funkcja ta 
    pozwala na ustalenie własnego czasu wykonania komendy po śmierci

V1.5.2
  * Poprawiliśmy błędy w plugine
  * Naprawiliśmy problem z klonujacymi się itemami
  * Dokonaliśmy kolejnej optymalizacji kodu
  * Usunęliśmy wsparcie dla wersji 1.12.x (Aktualne wsparcie dla wersji 1.13+)
  * Dodaliśmy wsparcie dla **ColorCodes** (&)

V1.5
  * ~~Dodaliśmy wsparcie dla wersji 1.12+~~

V1.4.2
  * Zoptymalizawaliśmy kod 
  * Dodaliśmy wiadomość która pokazuje się dla graczy którzy posiadają permisje **deathban.bypass**
  * Dodaliśmy licznik śmierci w **playerdata.yml**

V1.4.1
  * Postanowiliśmy usunąć narzuconą komendę przez plugin, teraz można używać własnych 
    komend

V1.3.3
  * ~~Naprawiliśmy problem z klonujacymi się itemami~~ - problem ten pojawiał się tylko gdy na serwerze zostało ustawione **KeepInventory**

V1.3.2
  * ~~Dodaliśmy losowy czas Bana do ustawienia w **Config.yml**~~ - plugin już nie korzysta z tej opcji pojawić się ona może w przyszłości

V1.3
  * Dodaliśmy **config.yml**
  * Zoptymalizowaliśmy kod

V1.2
  * ~~Dodaliśmy osobne zmienne (Czasu, Jednostki czasu, Powód Bana)~~ - plugin już nie korzysta z tej opcji pojawić się ona może w przyszłości
  
V1.1
  * Napisaliśmy w działający kod

## Co mamy dodać?

  * Możliwość wyłączenia działania pluginu
  * Wsparcie dla MySQL
  * Gui w którym można zarządzać całym pluginem
  * Zamierzamy dodać wsparcie dla wersji 1.8.x - 1.12.x
  * ~~Aktualnie korzystamy z bana z pluginu [Essentials](#https://www.spigotmc.org/resources/essentialsx.9089/) (/tempban), 
  zamierzamy dodać własnego bana który jest napisany w kodzie, chcemy dodać specjalną funkcję która będzie pozwalać na mnożenie ilości śmierci przez czas 
  (**Ilość śmierci** * **Przykładowy czas w ustawiony w config.yml** = **Czas bana**)~~
  * ~~Dodać licznik śmierci w pliku **data.yml**~~
