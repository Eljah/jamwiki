mvn package
cd javadiff
mvn install
cd ../jamwiki-core
mvn install
cd ../jamwiki-web
mvn install
cd ..
cp ~/workspace/jamwikibranch/jamwiki-war/target/jamwiki-war.war /opt/tomcat/webapps/