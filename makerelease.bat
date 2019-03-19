call svn update
call mvn release:prepare -DdryRun=true
call mvn release:clean release:prepare
cmd