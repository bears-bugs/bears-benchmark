#!/bin/bash

set -e

scriptDir=""

os=`uname`

# OS scecific initialization
case $os in
    Darwin)
        scriptDir=$(cd "$(dirname "$0")"; pwd)
    ;;
    Linux)
        scriptDir="$(dirname "$(readlink -f "$0")")"
    ;;
    *)
       echo "Unknown OS."
       exit 1
    ;;
esac

echo ${scriptDir}

if type blastn > /dev/null ; then
    echo "Blast already installed."
else
    distFilter=""
    case $os in
        Darwin)
            distFilter="universal-macosx"
        ;;
        Linux)
            distFilter="x64-linux"
        ;;
        *)
           echo "Unknown OS."
           exit 1
        ;;
    esac
    echo -n "Checking local blast version..."
    fileName=$(curl ftp://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/LATEST/ 2> /dev/null | grep -o -E 'ncbi-blast.*.gz$' | grep -o -E 'ncbi-blast.*.gz' | grep ${distFilter} )
    #echo ${fileName}
    blastVersion=$(echo ${fileName} | grep -o '[0-9]\+\.[0-9]\+\.[0-9]\++')
    #echo ${blastVersion}
    versionMarker="blast/ncbi-blast-${blastVersion}"
    if [ ! -f ${versionMarker} ]
    then
        echo " Upgrading..."
        rm -rf blast
        mkdir -p blast
        (cd blast; curl ftp://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/LATEST/${fileName} 2> /dev/null | tar -xzv )
        mv ${versionMarker} blast/blast
        touch ${versionMarker}
    else
        echo " Latest version is already installed.."
    fi
fi

if [ ! -f src/test/resources/big/16SMicrobial.nsq ]
then
    mkdir -p ${scriptDir}/src/test/resources/big
    (cd ${scriptDir}/src/test/resources/big; curl ftp://ftp.ncbi.nlm.nih.gov/blast/db/16SMicrobial.tar.gz 2> /dev/null | tar -xzv)
fi

