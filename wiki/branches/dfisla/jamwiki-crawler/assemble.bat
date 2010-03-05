
mkdir .\target\appassembler\etc
copy .\target\classes\*.* .\target\appassembler\etc
mvn package appassembler:assemble
