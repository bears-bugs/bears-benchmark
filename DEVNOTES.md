# Local Development

* Maven 3.5
* Java 8
* Dropwizard Docs: http://www.dropwizard.io/

### Check out repository:
```bash
git clone git@github.com:broadinstitute/consent-ontology.git
```

### Build, test
```bash
cd consent-ontology
mvn clean compile
APP_NAME=consent-ontology ENV=local OUTPUT_DIR=config ../firecloud-develop/configure.rb
```

Tests spin up an embedded http server that run against localhost. 
Ensure that your test environment supports that. 

#### Docker

Consent-Ontology is packaged into a docker image that is stored in the cloud in the [Consent-Ontology Dockerhub Repo](https://hub.docker.com/r/broadinstitute/consent-ontology).
```
# to build the image
./build.sh -d build

# to build the image and push to dockerhub
./build.sh -d push

# to pull the image from dockerhub
docker pull broadinstitute/consent-ontology
```

### Render Configs 
Specific to internal Broad systems:
```bash
APP_NAME=consent-ontology ENV=local OUTPUT_DIR=config ../firecloud-develop/configure.rb
```
Otherwise, use `src/test/resources/config.yml` as a template to 
create your own environment-specific configuration. 

### Spin up application:
Specific to internal Broad systems:
```bash
docker-compose -p consent-ontology -f config/docker-compose.yaml up
```
Or, if not using docker:
```bash
java -jar /path/to/consent-ontology.jar server /path/to/config/file
```

Visit local swagger page: https://local.broadinstitute.org:28443/swagger/

### Debugging
Port 5005 is open in the configured docker compose. 
Set up a remote debug configuration pointing to `local.broadinstitute.org`
and the defaults should be correct.

Execute the `fizzed-watcher:run` maven task (under consent plugins) 
to enable hot reloading of class and resource files.

### Developing with a local Elastic Search instance:

Update the compose file to include a new section for an ES instance:
```
es:
  image: docker.elastic.co/elasticsearch/elasticsearch:5.5.0
  ports:
    - "9200:9200"
  volumes:
    - ../data:/usr/share/elasticsearch/data
  environment:
    transport.host: 127.0.0.1
    xpack.security.enabled: "false"
    http.host: 0.0.0.0
```
Add a line to the `app` section to link to that:
```
  links:
    - es:es
```
Finally, update the servers in consent.conf to point to this instance:
```
elasticSearch:
  servers:
    - es
  indexName: local-ontology    
```

Consent will now point to a local ES instance. 
