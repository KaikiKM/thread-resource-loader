#!/bin/bash
set -e
openssl aes-256-cbc -K $encrypted_c97e58f83f35_key -iv $encrypted_c97e58f83f35_iv -in prepare_environment.sh.enc -out prepare_environment.sh -d
bash prepare_environment.sh
./gradlew
./gradlew uploadArchives
