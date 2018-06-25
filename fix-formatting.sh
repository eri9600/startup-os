#!/usr/bin/env bash

# Run it before committing to fix formatting
# on all files so you won't fail early on review
# Execute from repo root or, if using aa from base/head/startup-os

RED=$(tput setaf 1)
RESET=$(tput sgr0)

npm install &>/dev/null

BUILD_FILES=$(find `pwd` -type f \( -name BUILD.bazel -or -name BUILD \) | grep -v node_modules)

echo "$RED[:] Formatting BUILD files$RESET";
bazel run //tools:buildifier -- -mode=fix $BUILD_FILES &>/dev/null

echo "$RED[:] Formatting source files$RESET";
bazel run //tools/simple_formatter -- --path $(pwd) --java --python --proto --cpp --ignore_directories $(pwd)/node_modules/,$(pwd)/tools/local_server/web_login/node_modules/ &>/dev/null
