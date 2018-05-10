<!-- MarkdownTOC depth="4" autolink="true" bracket="round" -->

- [Overview](#overview)
    - [Usage](#usage)
    - [Template license](#template-license)

<!-- /MarkdownTOC -->

# Overview

A [Giter8][g8] template for a skeleton multi-SBT-project. The hint is in the name. 
Generates a starting point for a `http4s` and `doobie`-based REST app.

## Usage

```bash
sbt new https://github.com/leigh-perry/http4s-scalaz.g8
    name [SomeProjectName]:
    organisation [com.organisation]:
    package [com.organisation.someprojectname]:
    scala_version [2.12.6]:
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

## Template license

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.
