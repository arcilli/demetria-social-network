#!/bin/bash
# Make sure 3 replicas available
for rs in rs1 rs2 rs3; do
  mongo --host $rs --eval 'db'
  #  Wait until all the replicas are ready
  if [ $? -ne 0 ]; then
    exit 1
  fi
done

# Connect to rs1 and configure replica set if not done
status=$(mongo --host rs1 --quiet --eval 'rs.status().members.length')
if [ $? -ne 0 ]; then
  # ReplicaSet is not yed configured
  mongo --host rs1 --eval 'rs.initiate({ _id: "rs0", version: 1, members: [ { _id: 0, host : "rs1" }, { _id: 1, host : "rs2" }, { _id: 2, host : "rs3" } ] })'
fi