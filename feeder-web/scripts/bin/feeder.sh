#!/bin/bash

FEEDER_HOME=$(cd $(dirname $0)/..;pwd)
FEEDER_LIB=${FEEDER_HOME}/lib
FEEDER_LOGS=${FEEDER_HOME}/logs
SCRIPT_NAME=$(basename $0)
JAR_NAME=feeder-standalone.jar

function usage()
{
  echo "usage: ${SCRIPT_NAME}: [-dh] "
}

function execute()
{
  CONSOLE="false"
  ARGERR=0

  while getopts dh OPT; do
    case $OPT in
      d ) 
        CONSOLE="true"
        ;;
      h ) 
        usage
        exit 0
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

  #echo "java -Dlogdir=${FEEDER_LOGS} -Dtweet.console=${CONSOLE} -jar ${FEEDER_LIB}/${JAR_NAME}"
  java -Dlogdir=${FEEDER_LOGS} -Dtweet.console=${CONSOLE} -jar ${FEEDER_LIB}/${JAR_NAME} 
}

function main()
{
  execute $@
}

main $@
