
1.
image bauen:
docker buildx build -t graffitibackend_amd64 --platform linux/amd64 .

2.
image taggen:
docker tag graffitibackend_amd64:latest europe-west3-docker.pkg.dev/graffitibackendgcproject/lorenzwarerepo/graffitibackend_amd64:latest
3.
getaggtes image hochpushen:
docker push europe-west3-docker.pkg.dev/graffitibackendgcproject/lorenzwarerepo/graffitibackend_amd64:latest


4.
instanz starten:
mit env-vars (erstmal nicht notwendig)
 gcloud run deploy graffitibackend-amd64   --image europe-west3-docker.pkg.dev/graffitibackendgcproject/lorenzwarerepo/graffitibackend_amd64   --region europe-west3   --set-env-vars DB_HOST=34.159.104.28,DB_PORT=5432,DB_NAME=graffitidb,DB_USER=postgres,DB_PASSWORD=graffiti-password

ohne env-vars (weil in application-properties alles gesetzt):
gcloud run deploy graffitibackend-amd64   --image europe-west3-docker.pkg.dev/graffitibackendgcproject/lorenzwarerepo/graffitibackend_amd64   --region europe-west3 --port 3443

url:https://graffitibackend-amd64-586317135906.europe-west3.run.app/api/graffiti

achtung: backenddb habe ich gestoppt, hier wieder starten:https://console.cloud.google.com/sql/instances/graffitibackenddb/overview?cloudshell=true&hl=de&project=graffitibackendgcproject
Stand: uplaod von handy und postman funktionieren, von editor noch nicht...mal gucken was das problem ist