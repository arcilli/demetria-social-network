#/bin/bash
now="$(date +'%d-%m-%Y %H:%M:%S')"
jsonObject="{$now: "
## for each microservice that is available, write microservice name : memory info
containersStatus=$("docker stats --no-stream --format '{{.Container}}: {{.MemUsage}}'")
echo "$containerStatus"
jsonObject="$jsonObject }"
echo "$jsonObject"

