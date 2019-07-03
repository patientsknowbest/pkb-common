#!/usr/bin/env bash

# This script is called by Teamcity and it sets up parameters the artifact/image versioning

# sanity check: release tags should be increasing by 1 at a time
# 1,2,3,4 ..
TAGS_ORDER_BY_NO_DESC=`git tag | grep '^[1-9][0-9]*$' | sort -n -r`
TAG_DIFFS=`echo $TAGS_ORDER_BY_NO_DESC | awk 'BEGIN { RS=" "; FS=" " }  NR==1 {prev=$0; next} {print prev-$0; prev=$0}' | xargs echo 1 | tr ' ' '\n' | uniq`

if ! [[ $TAG_DIFFS == "1" ]]
then
    echo "# Error: Tags do not seem to be increasing by 1"
    echo $TAGS_ORDER_BY_NO_DESC
    echo Differences: $TAG_DIFFS
    exit 2
fi

LATEST_INTEGER_TAG=`echo $TAGS_ORDER_BY_NO_DESC | awk '{print $1}'`
NEXT_REV=$((LATEST_INTEGER_TAG + 1))
HEAD_SHA=`git rev-parse HEAD`
CLOSEST_TAG=`git describe --tags --abbrev=0`
CLOSEST_TAG_SHA=`git rev-list -n 1 $CLOSEST_TAG`
CURRENT_BRANCH=`git rev-parse --abbrev-ref HEAD`
CHANGELIST_NAME=`echo -$(git rev-parse --abbrev-ref HEAD | sed 's/[^a-zA-Z0-9]/_/g' | sed 's/master//g' | sed 's/develop//g') | sed 's/^-$//g'`

if [ "$HEAD_SHA" = "$CLOSEST_TAG_SHA" ] # we need to build a release version if HEAD is tagged
then
  # blow up build if (tag is integer && tag applies to the commit at HEAD && !(commit is a merge commit && current branch is master) # todo: for check merge commit as well
  if [ ! $CURRENT_BRANCH == "master" ] && [ $LATEST_INTEGER_TAG == $CLOSEST_TAG ]
  then
    echo "# Error: latest integer tag is attached to HEAD but current branch is not master"
    exit 3
  fi
  CHANGELIST=""
  echo "##teamcity[setParameter name='env.REVISION' value='$CLOSEST_TAG']"
else # otherwise just building a snapshot
  CHANGELIST="$CHANGELIST_NAME-SNAPSHOT"
  echo "##teamcity[setParameter name='env.REVISION' value='$NEXT_REV']"
fi
echo "##teamcity[setParameter name='env.CHANGELIST' value='$CHANGELIST']"

