#!/bin/bash

set -e

new_version=$1

git tag $new_version
git push --tags
