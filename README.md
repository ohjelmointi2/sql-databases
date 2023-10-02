# SQL-tietokantojen käyttö Javasta käsin

Tässä tehtävässä opettelemme muodostamaan yhteyden tietokantaan Java-ohjelmasta käsin ja tekemään yksinkertaisia CRUD-toimenpiteitä (Create, Read, Update & Delete). Tutustumme ohessa käsitteisiin kuten JDBC, DAO ja PreparedStatement.


## JDBC – Java Database Connectivity

[Javan standardikirjastoon määritelty JDBC (Java Database Connectivity) -ohjelmointirajapinta](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/) mahdollistaa Java-sovellusten yhdistämisen eri tyyppisiin SQL-tietokantoihin ja erilaisten kyselyiden sekä päivitysten tekemisen Java-koodista käsin.

JDBC ei rajoita sitä, minkä SQL-pohjaisten tietokantojen kanssa sitä voidaan käyttää, vaan eri tietokantoja voidaan hyödyntää käyttämällä niille toteutettuja valmiita ajureita. Sillä ei siis Java-koodisi näkökulmasta ole eroa, käytätkö tietokantana esimerkiksi [MySQL](https://www.mysql.com/)-, [PostgreSQL](https://www.postgresql.org/)- vai [SQLite](https://www.sqlite.org/index.html)-tyyppistä tietokantaa.

Tässä tehtävässä hyödynnämme **SQLite**-tietokantaa sen tiedostopohjaisuuden ja helppokäyttöisyyden vuoksi.


## SQLite

SQLite-tietokanta on paikallinen muisti- tai tiedostopohjainen tietokanta, joka ei vaadi erillistä palvelinta, vaan se voidaan "sulauttaa" osaksi omaa sovellustamme:

> *"In contrast to many other database management systems, SQLite is not a client–server database engine. Rather, it is embedded into the end program."*
>
> *"SQLite is a popular choice as embedded database software for local/client storage in application software such as web browsers. It is arguably the most widely deployed database engine, as it is used today by several widespread browsers, operating systems, and embedded systems (such as mobile phones), among others. SQLite has bindings to many programming languages.*"
>
> [https://en.wikipedia.org/wiki/SQLite](https://en.wikipedia.org/wiki/SQLite)

SQLite toimii Java-ohjelman näkökulmasta samalla tavalla kuin erilliset tietokantapalvelimet. Myös SQL-kyselyt ovat pääosin samat, esimerkiksi `SELECT ArtistId, Name FROM Artist`. "Keveydestään" ja tiedostopohjaisuudestaan huolimatta SQLite on erittäin merkityksellinen tietokanta ja sitä [käytetäänkin mm. suosituimmissa verkkoselaimissa ja puhelimissa](https://www.sqlite.org/famous.html):

> *"SQLite is built into all mobile phones and most computers and comes bundled inside countless other applications that people use every day."*
>
> https://www.sqlite.org/


## Ajurin lisääminen projektiin

SQLiten kanssa emme tarvitse erillistä tietokantapalvelinta, joten meidän ei tarvitse huolehtia verkkoyhteyksistä tai salasanoista. SQLite ei myöskään edellytä asennuksia, vaan riittää, että lisäämme [SQLite-ajurin](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/latest) Java-projektiimme.

SQLite-ajuri, kuten muutkin riippuvuudet, [voidaan ladata itse verkosta ja sijoittaa projektin hakemistoihin](https://www.google.com/search?q=add+jar+file+to+build+path). Riippuvuuksien hallinta on kuitenkin huomattavasti helpompaa, mikäli käytämme automaatiotyökalua kuten Gradle tai Maven. Tässä tehtäväpohjassa riippuvuus on valmiiksi määritettynä Gradle:n build.gradle-tiedostoon, joten riippuvuuden lataaminen ja tarvittavat asetukset tapahtuvat automaattisesti<sup>1</sup>:

```groovy
dependencies {
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    implementation 'org.xerial:sqlite-jdbc:3.43.0.0'
}
```

SQLite ei poikkea ajurin osalta muista tietokannoista. Käyttäessäsi MySQL-tietokantaa lisäisit riippuvuudeksi esimerkiksi. `'com.mysql:mysql-connector-j:8.1.0'`. Kaikki tämän tehtävän koodit toimivatkin myös esim. MySQL-tietokannoilla, kunhan käytät oikeaa ajuria ja yhteysosoitetta.

<small><sup>1</sup> 🤞 toivottavasti</small>


## Valmis musiikkitietokanta

Käytämme tässä tehtävässä valmista musiikkitietokantaa nimeltä [**Chinook**](https://github.com/lerocha/chinook-database):

> *"Chinook is a sample database available for SQL Server, Oracle, MySQL, etc."*
>
> *"The Chinook data model represents a digital media store, including tables for artists, albums, media tracks, invoices and customers."*
>
> [Luis Rocha, Chinook Database](https://github.com/lerocha/chinook-database)

Chinook-tietokanta sijaitsee valmiiksi tämän tehtäväpohjan [data](./data/)-hakemistossa.

Chinook-tietokanta sisältää lukuisia tietokantatauluja ja paljon valmista dataa, mutta tässä harjoituksessa käytämme ainoastaan `Artist`-, `Album`- ja `Track`-tauluja. Kaikki muut taulut voit jättää harjoitustyössäsi huomioimatta.

```mermaid
classDiagram
  direction LR
  class Album {
    AlbumId: INTEGER
    Title: NVARCHAR
    ArtistId: INTEGER
  }

  class Artist {
    ArtistId: INTEGER
    Name: NVARCHAR
  }

  class Track {
    TrackId: INTEGER
    Name: NVARCHAR
    AlbumId: INTEGER
    MediaTypeId: INTEGER
    GenreId: INTEGER
    Composer: NVARCHAR
    Milliseconds: INTEGER
    Bytes: INTEGER
    UnitPrice: NUMERIC
  }

  class MediaType {
    //...
  }

  class Genre {
    //...
  }

  Artist "1" --o "*" Album : Has Many
  Album "1" --o "*" Track : Has Many

  Track --|> MediaType: MediaTypeId
  Track --|> Genre: GenreId
```

⚠ **Voit vapaasti tutkia tietokannan sisältöä avaamalla [SQLite-komentorivityökalulla](https://sqlite.org/cli.html) tai jollain [lukuisista graafisista käyttöliittymistä](https://www.google.com/search?q=sqlite+gui). Älä kuitenkaan muuta `Artist`-taulun sisältöä. Muiden taulujen dataa voit muokata, lisätä ja poistaa vapaasti.**

Voit halutessasi tutustua myös muihin tätä tietokantaa käsitteleviin aineistoihin:

* UML-kaavio: [Chinook-tietokannan Wiki](https://github.com/lerocha/chinook-database/wiki/Chinook-Schema)
* Valmis tietokanta: [Chinook_Sqlite.sqlite](https://github.com/lerocha/chinook-database/raw/master/ChinookDatabase/DataSources/Chinook_Sqlite.sqlite)
* Dokumentaatio: https://github.com/lerocha/chinook-database
* SQL-luontikäskyt: [Chinook_Sqlite.sql](https://raw.githubusercontent.com/lerocha/chinook-database/master/ChinookDatabase/DataSources/Chinook_Sqlite.sql)
* Tietokannan lisenssi: [MIT](https://github.com/lerocha/chinook-database/blob/master/LICENSE.md)


## Pääohjelman suorittaminen

Tehtäväpohja sisältää valmiin pääohjelman [**JdbcDemoMain**](./src/main/java/databases/part01/JdbcDemoMain.java). Valmis pääohjelma auttaa sinua hahmottamaan ja kokeilemaan, miten yhteyksiä muodostetaan ja miten niiden avulla voidaan suorittaa kyselyitä. Voit suorittaa [pääohjelman](./src/main/java/databases/part01/JdbcDemoMain.java) joko koodieditorisi run-painikkeella tai Gradle:n avulla:

```sh
./gradlew run       # Unix
gradlew.bat run     # Windows
```

Kun suoritat ohjelman, se tulostaa kaikkien tietokannassa valmiiksi olevien artistien nimet järjestettynä niiden `ArtistId`:n mukaan:

```
AC/DC
Accept
Aerosmith
Alanis Morissette
Alice In Chains
...
```

## JDBC:n perusteet

Tietokantaoperaatiot tehdään JDBC:ssä kolmen keskeisen luokan avulla: **Connection**, **PreparedStatement** ja **ResultSet**. Näillä kolmella on keskeinen rooli tietokantaan yhteyden muodostamisessa, tietokantakyselyiden suorittamisessa ja tulosten käsittelyssä.

1. **Connection (yhteys):**
    - Yhteys mahdollistaa sovelluksen ja tietokannan välisen vuorovaikutuksen.
    - Yhteydenmuodostus vaatii tietokannan tiedot, kuten SQLite-tiedoston sijainnin. Se voi vaatia myös mm. tietokantapalvelimen osoitteen, käyttäjätunnuksen ja salasanan.
    - Yhteys tulee sulkea käytön jälkeen, jotta käytössä olevat resurssit vapautuvat uudelleenkäytettäviksi.

2. **PreparedStatement (SQL-lauseke):**
    - Tapa suorittaa SQL-kyselyitä tietokannassa Java-sovelluksessa.
    - Mahdollistaa SQL-kyselyjen parametrien syöttämisen turvallisesti.
    - Auttaa estämään SQL-injektiota.

3. **ResultSet (tulokset):**
    - ResultSet on tietokannasta saatava tulosjoukko, joka sisältää kyselyn tulokset.
    - ResultSetissä tiedot ovat organisoituina riveihin ja sarakkeisiin.
    - Java-sovellus voi lukea ResultSetistä tietoja ja käsitellä niitä tarpeen mukaan.
    - Tulostaulukkoa käytetään tavallisesti silmukan avulla, joka kulkee läpi tulokset ja noutaa tarvittavat tiedot.

Nämä luokat ja niiden väliset suhteet on havainnoillistettu seuraavassa kaaviossa:

```mermaid
classDiagram
    direction TB
    class DriverManager {
        Manages database connections
        +getConnection(url)
        +getConnection(url, user, password)
    }

    class Connection {
        Represents a database connection
        +prepareStatement(sqlString)
        +close()
    }


    class PreparedStatement {
        A precompiled SQL statement with parameters
        +setString(parameterIndex, text)
        +setInt(parameterIndex, number)
        +executeQuery()
        +executeUpdate()
        +close()
    }

    class ResultSet {
        Represents the result set of a query
        +next(): boolean
        +getString(columnIndex)
        +getInt(columnIndex)
        +close()
    }

    DriverManager --> Connection: obtains
    Connection --> PreparedStatement: creates
    PreparedStatement --> ResultSet: executes
```


## Osa 1: Kyselyn luonti ja tulosten käsittely *(perusteet, 20 %)*

Tehtävän ensimmäisessä osassa sinun tulee perehtyä [**JdbcDemoMain**](./src/main/java/databases/part01/JdbcDemoMain.java)-pääohjelmaluokkaan ja tehdä siihen kaksi pientä muutosta.

**Kyselyn muuttaminen**

Tietokantakyselyssä aineisto on järjestetty `ArtistId`-sarakkeen mukaan. Muuta kyselyä siten, että järjestät artistit aakkosjärjestykseen nimen mukaan.

**Tulosjoukon käsittely**

Pääohjelman alkuperäisessä versiossa jokaisen artistin kohdalla tulostetaan artistin nimi. Muuta ohjelmaa siten, että samalle riville, artistin nimen jälkeen, tulostetaan myös artistin id (`ArtistId`):

```
A Cor Do Som (43)
AC/DC (1)
Aaron Copland & London Symphony Orchestra (230)
Aaron Goldberg (202)
Academy of St. Martin in the Fields & Sir Neville Marriner (214)
```

💡 *Nyt artistit ovat hieman eri järjestyksessä ja esim. AC/DC ei ole enää ensimmäisenä.*

Tämä osa tehtävästä tarkastetaan tutkimalla ohjelmasi tulostetta, koska `System.out.println`-kutsuihin perustuvan ohjelmalogiikan testaaminen ohjelmallisesti on hankalaa. Tällainen lähestymistapa rajoittaa myös koodin uudelleenkäyttöä, koska main-metodi ei palauta mitään. Kun tarvitset artistien listausta myöhemmin toisessa osassa ohjelmaa, joudut toistamaan samaa logiikkaa, mikä on virhealtista ja tekee koodista hankalammin ylläpidettävää.

Parempi tapa on eristää logiikka omiin metodeihinsa, jotta sitä voidaan kutsua ohjelman muista osista tai muista ohjelmista. Ohjelman jakaminen osiin helpottaa siis sen **testaamista** ja tekee koodista **uudelleenkäytettävämpää** ja **ylläpidettävämpää**.


## Osa 2: Olioihin perustuva lähestymistapa *(perusteet, 40 %)*

Tehtävän toisessa osassa tehtävänäsi on hyödyntää olio-ohjelmointia ja jakaa tietokantaa käyttävät operaatiot tarkoituksenmukaisesti erillisiin luokkiin ja metodeihin.

**DAO (Data Access Object)**

Ohjelman rakenteen ja arkkitehtuurin suunnittelemiseksi on hyviä tunnettuja ja laajasti käytettyjä suunnittelumalleja (pattern), joita noudattamalla tulet soveltaneeksi hyviä käytäntöjä ja koodistasi tulee toivottavasti laadukasta. Ohjelmistokehittäjät noudattavat usein samoja suunnittelumalleja, mikä helpottaa muiden kirjoittamien ohjelmien ymmärtämistä ja koodauskäytäntöjen yhtenäistämistä.

Tietokantalogiikan eriyttämiseksi muusta koodista käytetään usein ns. DAO-mallia:

> *"A Data Access Object class can provide access to a particular data resource without coupling the resource's API to the business logic. For example, sample application classes access catalog categories, products, and items using DAO interface `CatalogDAO`."*
>
> Oracle. Data Access Object - Also Known As DAO. https://www.oracle.com/java/technologies/data-access-object.html


**Tehtävä**

Tehtäväpohjan paketissa [databases.part02](./src/main/java/databases/part02/) on valmiina luokat [Artist.java](./src/main/java/databases/part02/Artist.java), [ArtistDAO.java](./src/main/java/databases/part02/ArtistDAO.java) ja [ArtistAppMain.java](./src/main/java/databases/part02/ArtistAppMain.java):


1. **Artist.java:**

    Tämä luokka edustaa "Artist" -mallia (model) tai entiteettiä (entity). Luokka attribuutit ja metodit, jotka määrittelevät artistin rakenteen ja käyttäytymisen. Toisin sanoen se kapseloi artistin tiedot ja ominaisuudet, kuten artistin nimen ja id:n.

2. **ArtistDAO.java:**

    ArtistDAO (Data Access Object) -luokka toimii välittäjänä sovelluksen liiketoimintalogiikan ja tietokannan välillä. Sen pääasiallinen tehtävä on tarjota metodeja tietokantaoperaatioihin, jotka liittyvät "Artist" -entiteettiin, kuten artistien luontiin, hakemiseen, päivittämiseen ja poistamiseen. Se helpottaen muun sovelluksen työskentelyä tietokannan kanssa ilman tarvetta tuntea taustalla olevaa SQL:ää tai tietokantaan liittyviä yksityiskohtia.

3. **ArtistAppMain.java:**

    Tämä luokka toimii uutena pääohjelmana, joka hyödyntää ArtistDAO-luokkaa.

Tällainen vastuunjakaminen seuraa abstraktiuden ja modulaarisuuden periaatteita, mikä tekee sovelluksen kehittämisestä, ylläpidosta ja skaalautuvuudesta helpompaa. Näiden luokkien avulla edellisen osa 1:ssä käsitelty pääohjelma voisi näyttää seuraavalta:

```java
public static void main(String[] args) {
    ArtistDAO artistDAO = new ArtistDAO();
    List<Artist> artists = artistDAO.getArtists();

    for (Artist artist : artists) {
        System.out.println(artist.getName() + " (" + artist.getId() + ")");
    }
}
```

Tehtävän tässä osassa sinun tulee toteuttaa [ArtistDAO.java](./src/main/java/databases/part02/ArtistDAO.java)-luokkaan metodit `getArtists` sekä `getArtistById`. Metodien otsikot ja Javadoc-kommentit löytyvät luokasta valmiina.

Tällä kertaa ratkaisusi testataan yksikkötesteillä, jotka on kirjoitettu [ArtistDAOTest.java](./src/test/java/databases/part02/ArtistDAOTest.java)-luokkaan. Voit suorittaa testit joko koodieditorisi testaustyökalulla ([VS Code](https://code.visualstudio.com/docs/java/java-testing), [Eclipse](https://www.vogella.com/tutorials/JUnitEclipse/article.html)) tai [Gradle-automaatiotyökalulla](https://docs.gradle.org/current/userguide/java_testing.html):

```
./gradlew test --tests ArtistDAOTest      # unix
gradlew.bat test --tests ArtistDAOTest    # windows
```

💡 *Älä muuta testien toiminnan varmistamiseksi valmiiden metodien nimiä, parametreja tai paluuarvojen tyyppejä.*

💡 *Yritä välttää saman koodin toistamista molemmissa metodeissa. Saat toteuttaa tehtävänannossa mainittujen luokkien ja metodien lisäksi myös muita luokkia ja metodeja. Esimerkiksi `Database`-luokka yhteyksien avaamiseksi ja sulkemiseksi voi olla hyvä idea. Metodisi saavat myös kutsua toisiaan: voit kutsua getArtistById-metodissa getArtists-metodia (tehokkuudella ei tässä tehtävässä ole painoarvoa).*

💡 *Yhteyksien sulkeminen "käsin" vaatii monta operaatiota ja koodiriviä. Voit vaihtoehtoisesti perehtyä [Javan try-with-resources](https://www.baeldung.com/java-jdbc)-syntaksiin, jolla saat suljettua resurssit automaattisesti.*


## Osa 3: Tiedon lisääminen, päivittäminen ja poistaminen *(soveltaminen, 40 %)*

Edellisissä osissa olemme hakeneet tietoa `executeQuery`-metodilla. Tällä kertaa tarkoituksena on lisätä, päivittää ja poistaa tietoa `executeUpdate`-metodilla.

Tämän projektin paketista [databases.part03](./src/main/java/databases/part03/) löytyy luokat [Album](./src/main/java/databases/part03/Album.java) sekä [AlbumDAO](./src/main/java/databases/part03/AlbumDAO.java). Toteuta [AlbumDAO](./src/main/java/databases/part03/AlbumDAO.java)-luokkaan seuraavat operaatiot: `getAlbumsByArtist`, `addAlbum`, `updateAlbum` ja `deleteAlbum`.

Metodit löytyvät luokasta valmiina, ja niiden kommentit kuvailevat tarkemmin kultakin metodilta vaaditut toiminnot.


**SQL-injektiot ja tietoturva**

Huomaa, että SQL-kyselyjen muodostaminen merkkijonoja yhdistelemällä aiheuttaa tietoturvaongelmia, kuten alla oleva esimerkki havainnollistaa:

[![Exploits of a Mom](https://imgs.xkcd.com/comics/exploits_of_a_mom.png)](https://xkcd.com/327/)

*Kuva: Randall Munroe. Exploits of a Mom. [https://xkcd.com/327/](https://xkcd.com/327/). [CC BY-NC 2.5](https://creativecommons.org/licenses/by-nc/2.5/)*

Muista siis käyttää oppimateriaaleissa esiteltyä `PreparedStatement`-luokkaa ja sen `set`-metodeita aina muodostaessasi kyselyitä, joihin syötetään mukaan dataa!


**Ratkaisun testaaminen**

Albumien käsittelemiseksi ei ole valmista pääohjelmaa, mutta voit halutessasi luoda uuden pääohjelman, muokata edellisen osan ohjelmaa tai hyödyntää [tämän osan tarkastamiseksi kirjoitettuja yksikkötestejä](TODO). Testit voidaan suorittaa tuttuun tapaan koodieditorilla tai Gradlella:

```
./gradlew test --tests AlbumDAOTest      # unix
gradlew.bat test --tests AlbumDAOTest    # windows
```


## 🚀 Pro task: "kovakoodattu" yhteysosoite ympäristömuuttujaan

Usein samaa koodia suoritetaan lukuisissa erilaisissa ympäristöissä, kuten useiden eri kehittäjien omilla Windows-, Mac- ja Linux- koneilla. Kehittäjien henkilökohtaisten koneiden lisäksi saman koodin täytyy toimia testaus-, staging- ja tuotantoympäristössä, joka saattaa sijaita pilvipalvelussa tai omassa konesalissa. Eri ympäristöissä käytetään eri tietokantoja ja asetuksia, joten niissä tarvitaan eri yhteysosoitteet, käyttäjätunnukset ja muita muuttuvia tietoja esimerkiksi tietokantojen käyttämiseksi.

Ympäristökohtaisia asetuksia ei kirjoiteta suoraan ohjelmakoodiin, jotta koodia ei jouduta muuttamaan, kääntämään ja paketoimaan jokaista suoritusympäristöä varten.

Käyttäessämme SQLite-tietokantaa emme tarvitse erillisiä tunnuksia, koska tietokanta on käytännössä vain tiedosto paikallisessa järjestelmässä. Monien muiden tietokantaratkaisujen käyttämiseksi tarvitsisimme kuitenkin käyttäjätunnuksia ja salasanoja. Salasanoja ei koskaan haluta tallentaa selkokielisinä ohjelmakoodiin tai versionhallintaan.

Yleinen tapa ratkaista edellä esitettyjä ongelmia on asettaa ympäristökohtaisesti vaihtuvat sekä salaiset arvot käyttöjärjestelmän ympäristömuuttujiin. Sovellus voi ympäristömuuttujien avulla käyttää esimerkiksi kehitys-, testi- tai tuotantokantaa ilman, että ohjelmakoodia muutetaan. Salaiset tiedot, kuten salasanat, jäävät myös pois ohjelmakoodista.

Ympäristömuuttujat ovat eräänlainen käyttöjärjestelmäkohtainen Map-tietorakenne, jossa eri arvoja voidaan käsitellä avainten, eli ympäristömuuttujien nimien, avulla. Ympäristömuuttujien arvoja voidaan Javassa lukea `System.getenv`-metodilla esimerkiksi seuraavasti.

```diff
+ // merkkijono luetaan DATABASE-nimisestä ympäristömuuttujasta:
+ String connectionUrl = System.getenv("DATABASE");

- // kovakoodattu yhteysosoite:
- String connectionUrl = "jdbc:sqlite:data/Chinook_Sqlite.sqlite";
```

💡 *Huom! Tehtävien automaattisen arvioinnin vuoksi älä käytä ympäristömuuttujaa tehtävän palautuksessa.*


### Ympäristömuuttujien asettaminen

Voit asettaa VS Code:ssa ympäristömuuttujan muuttamalla ["Run and debug"-asetuksia](https://code.visualstudio.com/docs/java/java-debugging#_configuration-options) (ks. kohta `env`). Eclipsessä voit lisätä ohjelmallesi ympäristömuuttujia tämän [Stack Overflow -ketjun](https://stackoverflow.com/a/12810433) ohjeiden mukaisesti. Pidempi ohje löytyy tarvittaessa esimerkiksi [javacodegeeks.com:ista](https://examples.javacodegeeks.com/desktop-java/ide/eclipse/eclipse-environment-variable-setup-example/).

Vaihtoehtoisesti ympäristömuuttujia voidaan määritellä koko järjestelmän tasolla:

* [Windowsissa](https://www.google.com/search?q=windows+set+environment+variable)
* [Linuxissa](https://www.google.com/search?q=linux+set+environment+variable)
* [MacOS:ssa](https://www.google.com/search?q=macos+set+environment+variable).

Tällä kurssilla voi kuitenkin olla yksinkertaista asettaa ympäristömuuttuja vain omaan koodieditoriisi.


## 🚀 Pro task: Try-with-resources

Yhteyksien sulkeminen "käsin" vaatii monta operaatiota ja koodiriviä. Voit vaihtoehtoisesti perehtyä [Javan try-with-resources](https://www.baeldung.com/java-jdbc)-syntaksiin, jolla saat suljettua resurssit automaattisesti.


----


## Lisenssi ja tekijät

Tämän tehtävän on kehittänyt Teemu Havulinna ja se on lisensoitu [Creative Commons BY-NC-SA -lisenssillä](https://creativecommons.org/licenses/by-nc-sa/4.0/).

Tehtävänannon, käsiteltävien tiedostojen sekä lähdekoodien toteutuksessa on hyödynnetty ChatGPT 3.5:ttä sekä GitHub copilot-tekoälyavustinta.


## Chinook-tietokanta

Chinook-tietokannan on luonut [Luis Rocha](https://github.com/lerocha) ja se on lisensoitu avoimena lähdekoodina [MIT-lisenssillä](https://github.com/lerocha/chinook-database/blob/master/LICENSE.md).
