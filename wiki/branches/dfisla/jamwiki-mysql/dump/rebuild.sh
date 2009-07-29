#!/bin/bash
mysql -u admin --password=slivovica -e 'drop database jamwiki;'
mysql -u admin --password=slivovica -e 'create database jamwiki character set utf8;'
#mysql -u admin --password=slivovica jamwiki < jamwiki-clean-install-mysqldump-new2-noFK.sql
#mysql -u admin --password=slivovica jamwiki < jamwiki-clean-install-mysqldump-new3-utf-bin-blob.sql
#mysql -u admin --password=slivovica jamwiki < jamwiki-clean-install-mysqldump-new4-utf-bin-blob-MyISAM.sql
#mysql -u admin --password=slivovica jamwiki < jamwiki-clean-install-mysqldump-new6-utf-bin-blob-InnoDB.sql
mysql -u admin --password=slivovica jamwiki < jamwiki-clean-install-mysqldump-new6-utf-bin-blob-MyISAM.sql
