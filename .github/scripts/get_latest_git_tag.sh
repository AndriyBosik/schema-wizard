#!/bin/bash

set -e

latest_tag=$(git tag --sort=-v:refname | head -n 1)

if [ -z "$latest_tag" ]; then
  latest_tag="v0.0.0"
fi

echo "$latest_tag"
