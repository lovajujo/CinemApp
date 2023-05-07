# CinemApp
Kedves Hallgatótársam!

A CinemApp egy mozijegy foglaló alkalmazás, igen minimalista kivitelezésben. Nem sok mindent tud, de amit igen, azt röviden összefoglalom:
1. MainActivity és activity_main.xml
  Ez a nyitó oldal, bejelentkezési lehetőségekkel
2. RegistrationActivity és activiy_registration.xml
  Regisztráció megvalósítása (na ne xd)
3. TicketItem, TicketItemAdapter
  Mozijegy adatmodell
4. TicketActivity, list.xml, movies_menu.xml
  Itt vannak felsorolva a filmek, kosárba lehet rakni, ekkor a a menu sorban a kosár felett egy piros ponton megjelenik, hogy van valami a kosárba.
  Törölni is lehet egy filmet, ekkor törlődik a firestore-ból, illetve eltűnik a piros jelölőke a kosrá ikonról
5. NotificationHelper
  A kosárba helyezéskor értesítést küld (ez törléskor el is tűnik)
6. RandomAsyncLoader és RandomAsyncTask
  Bejelentkezéskor csak 10 mp után lehet csak vendégként belépni
  
A pontozási táblázatból az alábbiakat valósítottam meg:
-forditáso hiba nincs
-futási hiba nincs
-firebase autentikáció: bejelentkezés, regisztráció
-adatmodell definiálása (TicketItem)
-legalább 3 különböző activity használata (Main, Registration, Ticket)
-beviteli mezők beviteli típusa megfelelő
-constrain layout és egy mésik (realitve, linear)
-reszponzív: elforgatás esetén is igényes marad a layout (különböző méretekre nincs, csak landscape)
-legalább két különböző animáció (slide in és scale)
-intentek használata
-legalább egy lifecycle hook használata (onResumeban egy Toastot alkalmazok és az onPauseben sharedpreferencest)
-legalább egy olyan androidos erőforrás, amihez permission kell (location)
legalább egy notifiaction 
-CRUD műveletek
-csak 1 firestore leérdezés (filmek listázása aszerint, hogy hányszor raktuk kosárba)
-eléggé hajaz a videókra, meg tényleg minimailsta, sooo szubjektív pontból magamnak sem adnék sokat. De legalább működik és kész lett:D
Sok sikert az utolsó hétre és a vizsgaidőszakra!

(ps: Kecskeméten van olyan mozi, ahol érkezés alapján megy a helyfoglalás)
