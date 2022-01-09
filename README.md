# <a name="top"></a>GithubProfileViewer User Guide

[Opis projektu w języku polskim](#guide-pl)

1. [Overview](#overview-en)
2. [Using GPV](#usage-en)
    * [Instalation](#install-en)
        * [Installation on Apache Tomcat web server](#install-tomcat-en)
        * [Installation on other web servers](#install-other-en)
    * [Configuration](#config-en)
        * [Unauthorized mode](#config-unauth-en)
        * [Authorized mode](#config-auth-en)
3. [Possibilities for future functionalities](#possib-en)
    * [UI](#possib-ui-en)
        * [General remake of the user interface](#possib-ui-general-en)
        * [Using templates to generate output page](#possib-ui-templates-en)
        * [Language localization](#possib-ui-lang-en)
        * [Data presentation](#possib-ui-data-en)
    * [Filtering the user's repositories](#possib-filter-en)

## <a name="overview-en"></a>Overview

GithubProfileViewer (GPV) is a simple webapp that lets you list any github user's repositories, show the number of stargazers earned and list the most used languages through all the repositories.

`To list the most used languages you need to `[`configure`](#config-auth-en)` GPV to use Github API's authorization.`

GPV uses [json-simple](https://github.com/fangyidong/json-simple) library to parse Github API's responses.

## <a name="usage-en"></a>Using GPV
### <a name="install-en"></a>Instalation

GPV was created and tested with [Apache Tomcat 9.0.56](https://tomcat.apache.org).

#### <a name="install-tomcat-en"></a>Installation on Apache Tomcat web server

[Download](https://github.com/Acryval/GithubProfileViewer/releases/tag/v1.0.0) the GithubProfileViewer-X.X.X.war file

1. Tomcat Web App Deployment guide
    * Refer to the [Tomcat Web Application Deployment](https://tomcat.apache.org/tomcat-9.0-doc/deployer-howto.html)

2. Manual deployment
    * Put the file in under the `webapps` subdirectory inside your Apache Tomcat's installation directory
    * Restart Tomcat Web Server

#### <a name="install-other-en"></a>Installation on other web servers

To install GPV on other web servers please refer to your server's web app installation / deployment guide.

### <a name="config-en"></a>Configuration

Because of Github API's [request rate limitations](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting), requesting the languages used in all the user's repositories quickly drains the request count. That would result in HTTP 403 responses for users with more than 58 repos.

To overcome this limitation it is possible to use [authorized requests](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#authentication) to the Github API.

#### <a name="config-unauth-en"></a>Unauthorized mode

GPV's unauthorized mode lists the requested user's repositories, stargazers count for the repositories and the sum of all the stargazers across all the user's repositories.

There's no configuration needed to use GPV's unauthorized mode.

#### <a name="config-auth-en"></a>Authorized mode

GPV's authorized mode lists top used programming languages across all the user's repositories and total number of bytes of code written in those languages, in addition to the information gained from the unauthorized mode.

To access GPV's authorized mode:
1. [Create](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) github's personal access token (no scopes or permissions are necessary)
2. Edit the `web.xml` file associated with the GithubProfileViewer web app
    * In case of Apache Tomcat, it will be under the `<tomcat's-installation-dir>/webapps/GithubProfileViewer-X.X.X/WEB-INF/web.xml`
3. Under the `<servlet>` tag add
```
    <init-param>
        <param-name>oauth_token</param-name>
        <param-value>YOUR-OAUTH-TOKEN</param-value>
    </init-param>
```

You can also change the value of the `top_count` init parameter to match your desired number of top languages shown.

```
    <init-param>
        <param-name>top_count</param-name>
        <param-value>YOUR-DESIRED-TOP-COUNT</param-value>
    </init-param>
```

## <a name="possib-en"></a>Possibilities for future functionalities

#### 1. <a name="possib-ui-en"></a>UI

##### <a name="possib-ui-general-en"></a>General remake of the user interface

Both the initial `index.html` file and the resulting web page are not very pleasing to the eye. This is due to the time constraint imposed on the project. In the future, however, it is possible to change the start page or place the GPV reference itself on a different, larger page / in a bigger project.

##### <a name="possib-ui-templates-en"></a>Using templates to generate output page

In the case of the output page, it is hard-coded. It is a terrible technique that does not allow for any changes in the appearance of the website. In the future, one can add the option to use a previously prepared website template and 'pin up' relevant information in it in the right places. That way the app would not limit the possibilities / functionality of the resulting page.

##### <a name="possib-ui-lang-en">Language localization

As in the point above, the hard-coded output page does not allow you to change its language. Using templates, you can adjust the language of the page according to your current needs.

##### <a name="possib-ui-data-en"></a>Data presentation

One can also put the resulting data in JSON format, which would allow, for example, to save the data on a disk, in a database or to transfer this data to other web applications.

#### 2. <a name="possib-filter-en"></a>Filtering the user's repositories

In addition to listing all repositories of a given user, one could add the possibility to sort the repositories by the number of stars obtained, to filter them by the languages used in the project or by the people contributing to the repository.

# <a name="guide-pl"></a>GithubProfileViewer - Opis w języku polskim

1. [Przegląd](#overview-pl)
2. [Używanie GPV](#usage-pl)
    * [Instalacja](#install-pl)
        * [Instalacja na serwerze Apache Tomcat](#install-tomcat-pl)
        * [Instalacja na innych serwerach](#install-other-pl)
    * [Konfiguracja](#config-pl)
        * [Tryb nieautoryzowany](#config-unauth-pl)
        * [Tryb autoryzowany](#config-auth-pl)
3. [Możliwości przyszłego rozszerzenia projektu](#possib-pl)
    * [UI](#possib-ui-pl)
        * [Ogólne zmiany interfejsu użytkownika](#possib-ui-general-pl)
        * [Użycie szablonów do generowania strony wynikowej](#possib-ui-templates-pl)
        * [Lokalizacja językowa](#possib-ui-lang-pl)
        * [Prezentacja danych](#possib-ui-data-pl)
    * [Filtrowanie repozytoriów użytkownika](#possib-filter-pl)
    * [Lokalizacja językowa](#possib-lang-pl)

## <a name="overview-pl"></a>Przegląd

GithubProfileViewer (GPV) to prosta aplikacja webowa pozwalająca na wylistowanie repozytoriów danego użytkownika serwisu Github, pokazanie liczby gwiazdek zdobytych w poszczególnych repozytoriach czy wylistowanie najczęściej uzywanych języków programowania uzytych w repozytoriach.

`Aby wylistować najczęściej używane języki należy `[`skonfigurować`](#config-auth-pl)` GPV aby używał autoryzacji zapytań do Github API`

GPV uzywa biblioteki [json-simple](https://github.com/fangyidong/json-simple) do parsowania odpowiedzi od Github API.

## <a name="usage-pl"></a>Używanie GPV
### <a name="install-pl"></a>Instalacja

GPV został stworzony i był testowany przy użyciu serwera [Apache Tomcat 9.0.56](https://tomcat.apache.org).

#### <a name="install-tomcat-pl"></a>Instalacja na serwerze Apache Tomcat

[Ściagnij](https://github.com/Acryval/GithubProfileViewer/releases/tag/v1.0.0) plik GithubProfileViewer-X.X.X.war

1. Wdrożenie używając przewodnika [Tomcat Web Application Deployment](https://tomcat.apache.org/tomcat-9.0-doc/deployer-howto.html)
    * Wykonuj kroki zgodnie z przedstawioną instrukcją wdrożenia

2. Wdrożenie manualne
    * Umieść ściagniety plik w podfolderze `webapps` w folderze w którym został zainstalowany serwer Apache Tomcat
    * Zrestartuj serwer Tomcat

#### <a name="install-other-pl"></a>Instalacja na innych serwerach

Aby zainstalować GPV na serwerze innym niż Apache Tomcat, proszę odnieść się do instrukcji wdrożenia alkikacji dla uzywanego serwera

### <a name="config-pl"></a>Konfiguracja

Ze wzgledu na [limit częstości zapytań](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting) do Github API, podczas zapytań o języki używane w repozytoriach, dany limit jest szybko przekraczany. Wynikają z tego odpowiedzi HTTP 403 dla użytkowników z ilością repozytoriów większą niż 58.

Aby temu zapobiec, mozliwe jest użycie [zapytań autoryzowanych](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#authentication).

#### <a name="config-unauth-pl"></a>Tryb nieautoryzowany

Tryb nieautoryzowany pozwala na wylistowanie wszystkich repozytoriów danego użytkownika serwisu Github, ilości gwiazdek zdobytych w każdym z tych repozytoriów oraz sumy wszystkich zdobytych gwiazdek.

Do użycia trybu nieautoryzowanego, konfiguracja nie jest wymagana.

#### <a name="config-auth-pl"></a>Tryb autoryzowany

Tryb autoryzowany, oprócz informacji otrzymywanych w trybie nieautoryzowanym, dodatkowo pozwala na przedstawienie najczęściej używanych języków programowania oraz całkowitej ilości bajtów kodu napisanego w danych językach.

Aby użyć trybu autoryzowanego:
1. [Utwórz](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) peresonalny token uwierzytelniający (dodatkowe uprawnienia tokena nie są wymagane)
2. Edytuj plik `web.xml` powiązany z aplikacją GithubProfileViewer na serwerze
    * W przypadku Apache Tomcat, znajduje się on pod ścieżką `<katalog-instalacyjny-tomcat'a>/webapps/GithubProfileViewer-X.X.X/WEB-INF/web.xml`
3. W tagu `<servlet>` dodaj
```
    <init-param>
        <param-name>oauth_token</param-name>
        <param-value>TWOJ-TOKEN-UWIERZYTELNIAJACY</param-value>
    </init-param>
```

Można także zmienić wartość parametru `top_count` aby dostosować ilość najczęściej używanych języków do wyświetlenia.

```
    <init-param>
        <param-name>top_count</param-name>
        <param-value>ILOSC-JEZYKOW-DO-WYSWIETLENIA</param-value>
    </init-param>
```

## <a name="possib-pl"></a>Możliwości przyszłego rozszerzenia projektu

#### 1. <a name="possib-ui-pl"></a>UI

##### <a name="possib-ui-general-pl"></a>Ogólne zmiany interfejsu użytkownika

Zarówno plik początkowy `index.html` i wynikowa strona internetowa przedstawiajaca informacje nie wyglądają przyjemnie dla oka. Jest to spowodowane ograniczeniem czasowym nałożonym na projekt. W przyszłości jednak można zmienić stronę startową albo umieścić samo odniesienie do GPV w innej, większej stronie/projekcie.

##### <a name="possib-ui-templates-pl"></a>Użycie szablonów do generowania strony wynikowej

W przypadku strony wynikowej, jest ona na sztywno generowana z poziomu kodu. Jest to fatalna wręcz technika nie pozwalająca na jakiekolwiek zmiany w wyglądzie strony. W przyszłości można dodać możliwość użycia wcześniej przygotowanego szablonu strony internetowej i 'podopinać' w niej odpowiednie informacje w odpowiednich miejscach. Nie ograniczałoby to możliwości/funkcjonalności strony wynikowej.

##### <a name="possib-ui-lang-pl">Lokalizacja językowa

Podobnie jak w punkcie wyżej, sztywno generowana strona wynikowa nie pozwala na zmianę jej języka. Korzystając z szablonów mozna dostosować język strony wynikowej do aktualnych potrzeb.

##### <a name="possib-ui-data-pl"></a>Prezentacja danych

Można także wynikowe dane umieścić w formacie JSON, co pozwalałoby na np. zapisanie danych na dysku, w bazie danych czy na przekazanie tych danych innym aplikacjom webowym.

#### 2. <a name="possib-filter-pl"></a>Filtrowanie repozytoriów użytkownika

Oprócz samego listowania wszystkich repozytoriów danego użytkownika możnaby dodać mozliwość np sortowania ich po ilości zdobytych gwiazdek, filtrowania ich ze względu na języki użyte w projekcie czy ze względu na osoby przyczyniające się do rozwoju danego projektu.

#### [Powrót na początek dokumentu](#top)
