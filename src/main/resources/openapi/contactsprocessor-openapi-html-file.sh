#!/bin/sh

# In order to run below command, you first need to install redoc-cli
# by executing the following command on the command line: npm install -g redoc-cli
redoc-cli bundle -o contactsprocessor-openapi.html contactsprocessor-openapi.yaml
