# Activiti Cloud Application Service

The Activiti Cloud Application Service main purpose is to provide a view about the active (tracked) Activiti Cloud Application 
deployed inside an environment.  

This service interacts with the Service Registry and the Configuration Service in order to obtain which Applications 
(and their respective services), are up and running or pending to be deployed. 

Inside the configuration service, we have a set of JSON files that store the which applications are supposed to be deployed
and how these applications are supposed to be conformed. These structures (which are static and contain also 
service specific configurations) are used to compare against the Service Registry deployed services. 

*The spring boot starter here is used in the example https://github.com/Activiti/activiti-cloud-registry 
to provide a combined app service and registry. It can be run from there. For testing it can also be run from this repo.*

## Run from this repo for testing
For local testing there is a docker compose file inside the docker/ directory which can be used to bootstrap the required 
infrastructure plus an application. Each service inside the application should set a variable called: 
"activiti.cloud.application.name" which will be used by the Application Service to identify to which application each service
belong. This name will be also used to check in the configuration service the application "configuration"(structure).

Before starting the docker compose environment you need to set up your configuration store:
```$xslt
mkdir /tmp/config-repo
cd /tmp/config-repo
git init

```
Once you initialized your git repo store for your configurations you can add the following configuration files 
inside the repo and commit those changes

apps.json
```$xslt
{
  "appDeploymentEntries": [
    {
      "name": "default-app",
      "version": "1.0"
    }
  ]
}

```
and activiti-clouds-apps/default-app.json

```$xslt
{
  "applicationName": "default-app",
  "applicationVersion": "1.0",
  "serviceDeploymentDescriptors": [
            {
                "name": "rb-my-app",
                "version": "1.0",
                "serviceType": "runtime-bundle"
            },
            {
                "name": "activiti-cloud-query",
                "version": "1.0",
                "serviceType": "query"
            },
            {
                "name": "activiti-cloud-audit",
                "version": "1.0",
                "serviceType": "audit"
            }
        ]
}

```

after creating these files. You need to commit them to the git repo:
```$xslt
git add .
git commit -m "initial configs"
```

Then you can start the infrastructure. Notice that if you move the place where the git repo is initialized 
(/tmp/config-repo) you also need to change the volume mounted inside the docker-compose.yml file to reflect
this change.

Then you can start the infrastructure plus an application.

```$xslt
cd docker/
docker-compose up
```

Then start the application from src/test/resources

## Testing Deployed Applications

- Get the Directory of Deployment Descriptors

```$xslt
curl http://localhost:8090/v1/deployments/directory
``` 

Should return the list of deployment entries that are registered in the Config Service

```$xslt
{"appDeploymentEntries":[{"name":"default-app","version":"1.0"}]}
```

- Getting Deployment Descriptor for App
```$xslt
curl http://localhost:8090/v1/deployments/default-app
```

should return:
```$xslt
{
 "id":null,
 "applicationName":"default-app",
 "applicationVersion":"1.0",
 "serviceDeploymentDescriptors":[
    {
        "name":"rb-my-app",
        "version":"1.0",
        "serviceType":"RUNTIME_BUNDLE"
    },
    {
        "name":"activiti-cloud-query",
        "version":"1.0",
        "serviceType":"QUERY"
    },
    {
        "name":"activiti-cloud-audit",
        "version":"1.0",
        "serviceType":"AUDIT"
    }
  ],
  "deploymentDate":null
}
```

- Getting Running Apps with Status

```$xslt
curl http://localhost:8090/v1/apps/default-app
```

should return the current status of the app (or all apps if you don't specify the app name in the path)

```
{
    "id": "858eb5a3-5bfc-4147-b486-8e78345fa254",
    "name": "default-app",
    "version": null,
    "realm": null,
    "services": [
        {
            "id": "12259d5c-e82a-41c8-83ad-2d99d8aaf9f9",
            "serviceType": "RUNTIME_BUNDLE",
            "name": "rb-my-app",
            "version": null,
            "url": "http://rb-my-app:8081",
            "artifactName": null,
            "status": "UP"
        },
        {
            "id": "49a88689-8964-45d6-a8ae-db22ad275dc5",
            "serviceType": "QUERY",
            "name": "activiti-cloud-query",
            "version": null,
            "url": "http://activiti-cloud-query:8182",
            "artifactName": null,
            "status": "UP"
        },
        {
            "id": "531ac8d0-5da4-43d2-b9de-6f5e2d96623c",
            "serviceType": "AUDIT",
            "name": "activiti-cloud-audit",
            "version": null,
            "url": "http://activiti-cloud-audit:8181",
            "artifactName": null,
            "status": "UP"
        }
    ],
    "status": "UP",
    "projectRelease": null
}

```

