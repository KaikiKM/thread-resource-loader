#!/bin/bash
set -e
openssl aes-256-cbc -K $encrypted_0f1727a4905b_key -iv $encrypted_0f1727a4905b_iv -in prepare_environment.sh.enc -out prepare_environment.sh -d
bash prepare_environment.sh
./gradlew
./gradlew uploadArchives
