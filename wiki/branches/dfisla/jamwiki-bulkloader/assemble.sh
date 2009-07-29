#!/bin/bash
mvn package appassembler:assemble
mkdir ./target/appassembler/etc
cp ./target/classes/*.* ./target/appassembler/etc
