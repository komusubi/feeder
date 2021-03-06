#!/bin/bash

BUILD_HOME=$(cd $(dirname $0);pwd)

rpmbuild --define "_topdir "${BUILD_HOME}"" \
         --define "_builddir "${BUILD_HOME}/.."" \
         -ba ${BUILD_HOME}/SPECS/feeder.spec

