#!/bin/sh

# Create the Breton FSA spelling dictionary from
# Hunspell dictionary.
# 
# This creates the FSA spelling dictionary br_FR.dict.
#
# Author: Dominique Pelle <dominique.pelle@gmail.com>

file_oxt=difazier-an-drouizig-0_14.oxt
if [ ! -f $file_oxt ]; then
  # Download the Breton Hunspell dictionary, if not already done.
  wget http://extensions.libreoffice.org/extension-center/an-drouizig-breton-spellchecker/releases/0.14/$file_oxt
fi

# We're only interested in 2 files (*.aff and *.dic) files in the *.oxt zip file. 
unzip -o $file_oxt dictionaries/br_FR.aff dictionaries/br_FR.dic

TEMP_FILE=/tmp/lt-dictionary-br.dmp
 
unmunch dictionaries/br_FR.dic dictionaries/br_FR.aff |
sed -e "s/'/’/g" |
LC_ALL=C sort > $TEMP_FILE

java -cp ../../../../../../../../../../languagetool-standalone/target/LanguageTool-3.2-SNAPSHOT/LanguageTool-3.2-SNAPSHOT/languagetool.jar \
  -Xmx2048m \
  org.languagetool.dev.SpellDictionaryBuilder \
  br-FR \
  $TEMP_FILE br_FR.info - -o br_FR.dict

rm $TEMP_FILE
