Aplikacja używa Quarkus Application, ponieważ ma zwracac Uni lub Multi.


Java 21
Maven - do budowania projektu
Quarkus 3.19.0
Quarkus - framework do budowy aplikacji w Java
Mutiny - biblioteka do programowania reaktywnego w Quarkus
GitHub API - zewnętrzna usługa do pobierania informacji o repozytoriach
JUnit 5 - framework do testów
Rest Assured - narzędzie do testowania REST API



API
Endpoint: /github/repositories
np:
GET http://localhost:8080/github/repositories?username=kot


Obsługa błędów

{
    “status”: 404
    “message”: "User not found"
}


W pliku application.properties znajduje się konfiguracja dla GitHub API:
quarkus.rest-client."io.smallrye.mutiny.webclient.MutinyWebClient".url=https://api.github.com




