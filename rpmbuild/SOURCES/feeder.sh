#!/bin/bash

FEEDER_HOME=$(cd $(dirname $0)/..;pwd)
FEEDER_LIB=${FEEDER_HOME}/lib
FEEDER_LOGS=${FEEDER_HOME}/logs
FEEDER_CONF=${FEEDER_HOME}/conf
JAR_NAME=feeder-standalone.jar

usage()
{
  echo "$(basename $0): [-dh] [-t scraper|feeder] "
}

execute()
{
  CONSOLE="false"
  ARGERR=0

  while getopts dht: OPT; do
    case $OPT in
      d ) 
        CONSOLE="true"
        ;;
      h ) 
        usage
        exit 0
        ;;
      t ) 
        AGGREGATE_TYPE=$OPTARG
        ;;
      * )
        ARGERR=1
        ;;
    esac
  done
  
  if [ "${ARGERR}" -eq 1 ]; then
    usage
    exit 1
  fi

  if [ -z "$AGGREGATE_TYPE" ]; then
    usage
    exit 1
  fi

  cd ${FEEDER_CONF}
  java -Dlogdir=${FEEDER_LOGS} -Dtweet.console=${CONSOLE} -jar ${FEEDER_LIB}/${JAR_NAME} $AGGREGATE_TYPE
}

main()
{
  execute $@
}

main $@
