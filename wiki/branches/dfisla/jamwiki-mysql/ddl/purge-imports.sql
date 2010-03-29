use jamwiki;

-- jam_recent_change
delete from jam_recent_change where jam_recent_change.topic_id IN (select tv.topic_id from jam_topic_version tv, jam_topic t where t.topic_id = tv.topic_id and t.create_date > '2010-03-26');
commit;

-- jam_category
delete from jam_category where child_topic_id IN (select t.topic_id from jam_topic t where t.create_date > '2010-03-26');
commit;


-- jam_topic_version
alter table jam_topic_version disable keys;
set foreign_key_checks=0;
delete from jam_topic_version where topic_id IN (select t.topic_id from jam_topic t where t.create_date > '2010-03-26');
set foreign_key_checks=1;
alter table jam_topic_version enable keys;
commit;

-- jam_topic_cache
alter table jam_topic_cache disable keys;
set foreign_key_checks=0;
delete from jam_topic_cache where topic_id IN (select t.topic_id from jam_topic t where t.create_date > '2010-03-26');
set foreign_key_checks=1;
alter table jam_topic_cache enable keys;
commit;

-- jam_topic
alter table jam_topic disable keys;
set foreign_key_checks=0;
delete from jam_topic where create_date > '2010-03-26';
set foreign_key_checks=1;
alter table jam_topic enable keys;
commit;
