<!-- MarkdownTOC depth="4" autolink="true" bracket="round" -->

- [Sample project](#sample-project)

<!-- /MarkdownTOC -->

# Sample project

```bash
sbt new https://github.com/leigh-perry/sbt-multi-project.g8
    name [SomeProjectName]:
    organisation [com.organisation]:
    package [com.organisation.someprojectname]:
    scala_version [2.12.4]:
    sbt_version [0.13.16]:

    Template applied in ./someprojectname

cd someprojectname
sbt compile test docker

cd testing
docker-compose -f docker-compose-extapi.yml up -d

curl -v -i 0.0.0.0:6789/test
curl -v -i 0.0.0.0:6789/hourly
curl -v -i 0.0.0.0:6789/hourly/2

docker-compose -f docker-compose-extapi.yml down
```
