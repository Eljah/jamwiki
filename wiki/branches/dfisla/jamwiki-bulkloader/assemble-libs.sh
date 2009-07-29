#!/bin/bash
#mkdir ./target/appassembler/etc
#cp ./target/classes/*.* ./target/appassembler/etc
mvn package appassembler:assemble
