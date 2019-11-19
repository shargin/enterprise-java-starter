<?xml version="1.0" encoding="UTF-8"?>
<server description="OpenLiberty Server">

  <jdbcDriver id="postgresql-driver" javax.sql.XADataSource="org.postgresql.xa.PGXADataSource"
      javax.sql.ConnectionPoolDataSource="org.postgresql.ds.PGConnectionPoolDataSource"
      libraryRef="postgresql-library"/>

  <library id="postgresql-library">
    <fileset id="PostgreSQLFileset" dir="/opt/wlp/lib" includes="postgresql-9.4.1212.jar"/>
  </library>

</server>
