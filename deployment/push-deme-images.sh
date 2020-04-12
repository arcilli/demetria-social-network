#!/bin/bash
# Try not to use this since it's using custom version of images.
# Generalize only custom versions (or the latest one).
docker push arcilli/demetria-user-service:2.0-SNAPSHOT
docker push arcilli/demetria-friendship-relation-service:2.0-SNAPSHOT
docker push arcilli/demetria-front-end:2.0-SNAPSHOT
docker push arcilli/demetria-post-service:2.0-SNAPSHOT