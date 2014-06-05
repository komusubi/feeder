#!/bin/bash

FEEDER_HOME=$(cd $(dirname $0)/..;pwd)
FEEDER_LIB=${FEEDER_HOME}/lib
FEEDER_LOGS=${FEEDER_HOME}/logs
FEEDER_CONF=${FEEDER_HOME}/conf
SCRIPT_NAME=$(basename $0)
JAR_NAME=feeder-standalone.jar

function usage()
{
  echo "usage: ${SCRIPT_NAME}: [-cdhs] [-t scraper|feeder] [-u jal5971|jal5931]"
}

function execute()
{
  CONSOLE="false"
  FEEDER_CACHE="true"
  STORAGE="true"
  ARGERR=0

  while getopts cdhst:u: OPT; do
    case $OPT in
      c )
        FEEDER_CACHE="false"
        ;;
      d ) 
        CONSOLE="true"
        ;;
      h ) 
        usage
        exit 0
        ;;
      s )
        STORAGE="false"
        ;;
      t ) 
        AGGREGATE_TYPE=$OPTARG
        ;;
      u )
        SCRAPE_TYPE=$OPTARG
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

  if [ -z "${AGGREGATE_TYPE}" ] || [ -z "${SCRAPE_TYPE}" ]; then
    usage
    exit 1
  fi

  cd ${FEEDER_CONF}
  java -Dlogdir=${FEEDER_LOGS} -Dtweet.console=${CONSOLE} \
       -Dmessage.storage=${STORAGE} -Dfeeder.history=${FEEDER_CACHE} -Dfeeder.home=${FEEDER_HOME} \
       -jar ${FEEDER_LIB}/${JAR_NAME} ${AGGREGATE_TYPE} ${SCRAPE_TYPE}
}

function main()
{
  execute $@
}

main $@
