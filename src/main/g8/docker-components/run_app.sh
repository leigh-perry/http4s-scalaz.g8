#!/usr/bin/env bash

# This runs inside the application docker container, all at /app
SCRIPT_PATH=\$(dirname "\$0")

JAR_FILE=\$1
GLOBAL_SWITCHES="--add-modules java.xml.bind"

echo JAR_FILE=\${JAR_FILE}
env | grep XAPP_ | sort

if [[ "\$XAPP_OPERATION" == "extapi" ]]; then
    java -cp "\${JAR_FILE}" \${GLOBAL_SWITCHES} $package$.extapi.AppMain

else
  echo "Unknown option '\$XAPP_OPERATION'"

fi
