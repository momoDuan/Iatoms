@REM
set "folderPath=C:\Users\riverjin\.m2\repository"
set "jarFolderPath=D:\project\IATOMS\SVN\5_DEV\51_SRC\trunk\lib"

@REM cafe-framework jar
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-cm -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-cm-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-core -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-core-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-identity -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-identity-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-workflow -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-workflow-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"

cmd

