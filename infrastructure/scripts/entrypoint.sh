#!/bin/bash
set -e

staticConfigFile=/opt/couchbase/etc/couchbase/static_config
restPortValue=8091

function overridePort() {
    portName=$1
    portNameUpper=$(echo $portName | awk '{print toupper($0)}')
    portValue=${!portNameUpper}

    # only override port if value available AND not already contained in static_config
    if [ "$portValue" != "" ]; then
      if grep -Fq "{${portName}," ${staticConfigFile}
      then
        echo "Don't override port ${portName} because already available in $staticConfigFile"
      else
        echo "Override port '$portName' with value '$portValue'"
        echo "{$portName, $portValue}." >> ${staticConfigFile}

        if [ ${portName} == "rest_port" ]; then
          restPortValue=${portValue}
        fi
      fi
    fi
}

overridePort "rest_port"
overridePort "mccouch_port"
overridePort "memcached_port"
overridePort "query_port"
overridePort "ssl_query_port"
overridePort "fts_http_port"
overridePort "moxi_port"
overridePort "ssl_rest_port"
overridePort "ssl_capi_port"
overridePort "ssl_proxy_downstream_port"
overridePort "ssl_proxy_upstream_port"

[[ "$1" == "couchbase-server" ]] && {

  if [ $(whoami) = "couchbase" ]; then
    if [ ! -w /opt/couchbase/var -o \
     $(find /opt/couchbase/var -maxdepth 0 -printf '%u') != "couchbase" ]; then
       echo "/opt/couchbase/var is not owned and writable by UID 1000"
       echo "Aborting as Couchbase Server will likely not run"
       exit 1
    fi
  fi
  echo "Staring Couchbase Server -- Web UI available at http://<ip>:$restPortValue"
  echo "and logs available in /opt/couchbase/var/lib/couchbase/logs"
  ./configure-node.sh & exec runsvdir -P /etc/service 'log: ...........................................................................................................................................................................................................................................................................................................................................................................................................'

}

exec "$@"