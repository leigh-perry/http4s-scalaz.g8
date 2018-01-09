A [Giter8][g8] template for a skeleton multi-SBT-project. The hint is in the name.

Manual
```bash
cd ~/temp
rm -rf SomeProjectName
sbt new file:///Users/lperry/dev/lp/sbt-multi-project.g8/
rm -rf target
cd someprojectname/
export JAVA_HOME=(/usr/libexec/java_home -v 1.8.0_144)
sbt test
```

NFG
```bash
export JAVA_HOME=(/usr/libexec/java_home -v 1.8.0_144)
sbt g8Test
```

Template license
----------------

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.
