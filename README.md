# Hondenschool Wedstrijden

## Beschrijving thema
Het thema van dit project is **hondenschool wedstrijden**. Hierbij nemen honden en hun begeleiders (personen) deel aan wedstrijden, en wordt bijgehouden welke score ze behalen.

Er zijn vier kernentiteiten:

- **Hond**: bevat een unieke ID en bijkomende gegevens (bv. naam, ras, leeftijd).  
- **Persoon**: bevat een unieke ID en bijkomende gegevens (bv. naam, adres, rol als eigenaar of begeleider).  
- **Wedstrijd**: bevat een unieke ID en bijkomende gegevens (bv. datum, locatie, type wedstrijd).  
- **History**: legt de relatie tussen hond, persoon en wedstrijd vast, inclusief de behaalde **score**.  

Deze structuur maakt het mogelijk om een volledig overzicht te krijgen van de deelnames van honden en personen aan wedstrijden.  


## Microservices & Componenten
Het project is opgebouwd uit verschillende microservices en ondersteunende componenten:

- **Hond-service** – beheer van honden en hun gegevens.  
- **Persoon-service** – beheer van personen en hun gegevens.  
- **Wedstrijd-service** – beheer van wedstrijden.  
- **History-service** – legt de koppelingen vast tussen hond, persoon en wedstrijd.  
- **API Gateway** – fungeert als toegangspunt tot alle microservices.  
- **Prometheus** – monitoring van de microservices.  
- **Grafana** – visualisatie van de monitoring.


## Hosting & Toegang
Het volledige project is gedeployed in **Kubernetes**. Om publieke toegang mogelijk te maken zijn tunnels voorzien naar de API, Prometheus en Grafana.

- [API Gateway] https://aptneleapigateway.krejo.be
- [Prometheus] https://aptneleprometheus.krejo.be 
- [Grafana] https://aptnelegrafana.krejo.be  (username: admin, password: admin)


## Schema
![Schema Hondenschool Wedstrijden](/Screenshots/dogCompetitions.drawio.png)


## Uitbreidingen
- Hosting in Kubernetes  
- Tunnels voor publieke toegang  
- Integratie van Prometheus voor monitoring 
- Integratie van Grafana voor visualisatie van de monitoring   

## Screenshots
Deze requests zijn allemaal uitgevoerd op de api-gateway en worden daardoor gemapt naar de corresponderende microservices.

![screenshot](/Screenshots/dog-POST.png)
![screenshot](/Screenshots/dog-PUT.png)
![screenshot](/Screenshots/dog-GET.png)
![screenshot](/Screenshots/dog-GET%20all.png)


![screenshot](/Screenshots/person-POST.png)
![screenshot](/Screenshots/person-PUT.png)
![screenshot](/Screenshots/person-GET.png)
![screenshot](/Screenshots/person-GET%20all.png)

![screenshot](/Screenshots/competition-POST.png)
![screenshot](/Screenshots/competition-PUT.png)
![screenshot](/Screenshots/competition-GET.png)
![screenshot](/Screenshots/competition-GET%20all.png)


![screenshot](/Screenshots/history-POST.png)
![screenshot](/Screenshots/history-PUT.png)
![screenshot](/Screenshots/history-DELETE.png)
![screenshot](/Screenshots/history-GET.png)
