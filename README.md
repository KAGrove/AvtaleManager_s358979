# AvtaleManager_s358979

## Innledning

Dette prosjektet er en Android-applikasjon utviklet som en del av en skoleoppgave. Applikasjonen har som mål å hjelpe brukere med å holde styr på sine avtaler og venner.

## Funksjonaliteter

- **SQLite Database**: All informasjon om avtaler og venner lagres i en SQLite-database på telefonen.
- **Liste over Avtaler**: Når appen åpnes, vises en liste over alle avtaler.
- **Venner og Preferanser**: Brukeren kan navigere til skjermer som viser en liste over alle registrerte venner og til preferanser.
- **Daglig Sjekk og Notifikasjoner**: Appen sjekker databasen en gang i døgnet og sender en SMS til de relevante vennene hvis det finnes en avtale.
- **Preferanser**: Brukeren kan slå SMS-tjenesten av og på, velge tidspunkt for når SMS skal sendes, og sette en standardmelding for SMS.

## Teknologier

- **Språk**: Java
- **Database**: SQLite via Room-biblioteket
- **Andre Teknologier**: Android SDK, BroadcastReceivers, AlarmManager, SharedPreferences

## Klasser og Entiteter

- `Avtale`: Representerer en avtale med felter som dato, klokkeslett, og treffsted.
- `Kontakt`: Representerer en venn med felter som fornavn, etternavn, og telefonnummer.
- `Deltakelse`: En hjelpetabell som representerer mange-til-mange-relasjonen mellom `Avtale` og `Kontakt`.

## Hvordan kjøre prosjektet

1. Klone repositoriet til din lokale maskin.
2. Åpne prosjektet i Android Studio.
3. Kjør appen på en Android-emulator eller en fysisk enhet.

## Kontakt

For spørsmål eller mer informasjon, vennligst ta kontakt.
