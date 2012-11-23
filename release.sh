#!/bin/bash
VERSION=$1
if [[ -n "$VERSION" ]]; then
    VERSION=" -DreleaseVersion=$VERSION"
fi

mvn release:prepare -Denv=release-plugin $VERSION -Dresume=false -B && mvn release:perform -Denv=release-plugin
