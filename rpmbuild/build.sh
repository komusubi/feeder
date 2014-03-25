#!/bin/bash

rpmbuild --define "_topdir /home/jun/workspace/feeder/rpmbuild" \
         --define "_builddir /home/jun/workspace/feeder" \
         -bi SPECS/feeder.spec

