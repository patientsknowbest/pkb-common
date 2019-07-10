#!/usr/bin/env bash

# This script is called by Teamcity to grab the coding guidelines spec from phr so we can run static analysis with it

if [ -z "$GH_TOKEN" ];
then
    echo "# Error: No GH token for static code analysis download"
    exit 2  
else
    curl -H "Authorization: token $GH_TOKEN" -O https://raw.githubusercontent.com/patientsknowbest/phr/develop/.idea/inspectionProfiles/pkb_coding_guidelines.xml
fi