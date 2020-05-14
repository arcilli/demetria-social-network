#!/bin/sh
docker service rm deme_front-end deme_friendship-relation deme_user-service deme_orient-db deme_post-service deme_rs1 deme_rs2 deme_rs3 deme_search-service
docker service rm deme_rs-setup deme_orient-db-initial-setup
