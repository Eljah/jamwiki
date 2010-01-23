--delete from jam_topic where jam_topic.topic_id IN (select jt.topic_id from jam_topic jt, jam_topic_version jtv where jt.topic_id = jtv.topic_id and jtv.edit_comment = "imported");

delete from jam_recent_change where jam_recent_change.topic_id > 4;


alter table jam_topic disable keys;
set foreign_key_checks=0;
delete from jam_topic where jam_topic.topic_id > 4;
set foreign_key_checks=1;
alter table jam_topic enable keys;
commit;

alter table jam_topic_version disable keys;
set foreign_key_checks=0;
delete from jam_topic_version where edit_comment = "imported";
set foreign_key_checks=1;
alter table jam_topic_version enable keys;
commit;