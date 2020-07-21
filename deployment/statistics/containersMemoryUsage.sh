#/bin/bash
now="$(date +'%d-%m-%Y %H:%M:%S')"
jsonObject="{$now: "
## for each microservice that is available, write microservice name : memory info
now="$(date +'%d-%m-%Y %H:%M:%S')"
jsonObject="{$now: "
## for each microservice that is available, write microservice name : memory info
containersStatus="$(docker stats --no-stream --format 'table {{.Name}}: {{.MemUsage}}')"
##echo "1: $containersStatus"
for container in $containersStatus
do
  echo $container
  #echo dgasda
  #$jsonObject=$jsonObject$container
done
#jsonObject="$jsonObject }"
#echo "$jsonObject"
