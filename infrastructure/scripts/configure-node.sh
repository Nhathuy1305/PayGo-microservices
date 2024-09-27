set -m

### DEFAULTS
NODE_TYPE=${NODE_TYPE:='DEFAULT'}
CLUSTER_USERNAME=${CLUSTER_USERNAME:='Administrator'}
CLUSTER_PASSWORD=${CLUSTER_PASSWORD:='password'}
CLUSTER_RAMSIZE=${CLUSTER_RAMSIZE:=300}
SERVICES=${SERVICES:='data,index,query,fts,eventing'}
BUCKET=${BUCKET:='default'}
BUCKET_RAMSIZE=${BUCKET_RAMSIZE:=100}
BUCKET_TYPE=${BUCKET_TYPE:=couchbase}
RBAC_USERNAME=${RBAC_USERNAME:=$BUCKET}
RBAC_PASSWORD=${RBAC_PASSWORD:=$CLUSTER_PASSWORD}
RBAC_ROLES=${RBAC_ROLES:='admin'}

sleep 2
echo ' '
printf 'Waiting for Couchbase Server to start'
until $(curl --output /dev/null --silent --head --fail -u $CLUSTER_USERNAME:$CLUSTER_PASSWORD http://localhost:8091/pools); do
  printf .
  sleep 1
done

echo ' '
echo Couchbase Server has started
echo Starting configuration for $NODE_TYPE node

echo Configuring Individual Node Settings
/opt/couchbase/bin/couchbase-cli node-init \
  --cluster localhost:8091 \
  --user=$CLUSTER_USERNAME \
  --password=$CLUSTER_PASSWORD \
  --node-init-data-path=${NODE_INIT_DATA_PATH:='/opt/couchbase/var/lib/couchbase/data'} \
  --node-init-index-path${NODE_INIT_INDEX_PATH:='/opt/couchbase/var/lib/couchbase/indexes'} \
  --node-init-hostname=${NODE_INIT_HOSTNAME:='127.0.0.1'} \
> /dev/null

