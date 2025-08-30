#!/bin/sh
# entrypoint.sh

# Exit immediately if a command exits with a non-zero status.
set -e

# Initialize JAVA_TOOL_OPTIONS to ensure it's not unset.
export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS}"

# Check for each LDAP environment variable and convert it into a
# Java system property (e.g., LDAP_HOST -> -Dldap.host=...)
# This allows any combination of the variables to be provided at runtime.

if [ -n "$MONGO_URL" ]; then
	echo "✅ MONGO_URL is set. Adding to Java system properties."
	export JAVA_TOOL_OPTIONS="-Dmongo.url=${MONGO_URL} ${JAVA_TOOL_OPTIONS}"
fi

if [ -n "$MONGO_USERNAME" ]; then
	echo "✅ MONGO_USERNAME is set. Adding to Java system properties."
	export JAVA_TOOL_OPTIONS="-Dmongo.username=${MONGO_USERNAME} ${JAVA_TOOL_OPTIONS}"
fi

if [ -n "$MONGO_PASSWORD" ]; then
	echo "✅ LDAP_PASSWORD is set. Adding to Java system properties."
	# Note: Be cautious about logging or exposing secrets. Here we just confirm it's set.
	export JAVA_TOOL_OPTIONS="-Dmongo.password=${MONGO_PASSWORD} ${JAVA_TOOL_OPTIONS}"
fi

echo "🚀 Starting application..."

# Execute the command passed to this script (the CMD from the Dockerfile).
exec "$@"
