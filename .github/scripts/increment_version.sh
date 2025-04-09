#!/bin/bash

set -e

current_version=$1
release_type=$2

if [ -z "$current_version" ] || [[ -z "$release_type" ]]; then
  echo "Usage: $0 <current_version> <release_type>"
  echo "current_version should be in tht following format: v<major>.<minor>.<patch>"
  echo "release_type must be one of: major, minor, patch"
  exit 1
fi

version="${current_version#v}"

# Split version into components
IFS='.' read -r major minor patch <<< "$version"

# Increment based on release type
case "$release_type" in
  patch)
    patch=$((patch + 1))
    ;;
  minor)
    minor=$((minor + 1))
    patch=0
    ;;
  major)
    major=$((major + 1))
    minor=0
    patch=0
    ;;
  *)
    echo "Invalid release_type: $release_type"
    echo "Must be one of: major, minor, patch"
    exit 1
    ;;
esac

echo "v$major.$minor.$patch"
