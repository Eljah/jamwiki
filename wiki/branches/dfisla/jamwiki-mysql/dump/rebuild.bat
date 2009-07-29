mysql -u admin --password=slivovica -e "drop database jamwiki;"
mysql -u admin --password=slivovica -e "create database jamwiki character set utf8;"
mysql -u admin --password=slivovica jamwiki < jamwiki-clean-install-mysqldump-new8-InnoDB.sql
