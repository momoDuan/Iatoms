@REM
set "folderPath=D:\m2\repository"
set "jarFolderPath=D:\IATOMS\5_DEV\51_SRC\trunk\lib"

call mvn deploy:deploy-file -DgroupId=com-google-gson -DartifactId=gson -Dversion=2.3.1 -Dfile="%jarFolderPath%\json\gson-2.3.1.jar" -Dpackaging=jar -Durl="file://%folderPath%\"

@REM secutity
call mvn deploy:deploy-file -DgroupId=com.cybersoft4u.framework -DartifactId=cyber-iatoms-security -Dversion=1.0 -Dfile="%jarFolderPath%\security\cyber-iatoms-security-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=org.owasp -DartifactId=antisamy -Dversion=1.4.3 -Dfile="%jarFolderPath%\security\antisamy-1.4.3.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=org.owasp -DartifactId=ESAPI -Dversion=2.0.1 -Dfile="%jarFolderPath%\security\esapi-2.0.1.jar" -Dpackaging=jar -Durl="file://%folderPath%\"

@REM cafe-framework jar
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-cm -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-cm-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-core -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-core-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-identity -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-identity-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-workflow -Dversion=1.0 -Dfile="%jarFolderPath%\cafe\cafe-workflow-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"

@REM mq
call mvn deploy:deploy-file -DgroupId=com.ibm.mqjms -DartifactId=com.ibm.mqjms -Dversion=5.2 -Dfile="%jarFolderPath%\mq\com.ibm.mqjms-5.2.jar" -Dpackaging=jar -Durl="file://%folderPath%\"

@REM J2EE jar
call mvn deploy:deploy-file -DgroupId=j2ee -DartifactId=j2ee -Dversion=1.4.1 -Dfile="%jarFolderPath%\j2ee\j2ee-1.4.1.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=javax.resource -DartifactId=connector-api -Dversion=1.5 -Dfile="%jarFolderPath%\j2ee\connector-api-1.5.jar" -Dpackaging=jar -Durl="file://%folderPath%\"

@REM common jar
call mvn deploy:deploy-file -DgroupId=sun.jai -DartifactId=jai_codec -Dversion=1.1.2 -Dfile="%jarFolderPath%\common\jai_codec-1.1.2.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=sun.jai -DartifactId=jai_core -Dversion=1.1.2 -Dfile="%jarFolderPath%\common\jai_core-1.1.2.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=javax.xml -DartifactId=jaxrpc-api -Dversion=1.4.2 -Dfile="%jarFolderPath%\common\jaxrpc-api-1.4.2.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=jdsl -DartifactId=jdsl -Dversion=2.1.1 -Dfile="%jarFolderPath%\common\jdsl-2.1.1.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=log4j -DartifactId=log4j -Dversion=1.2.15 -Dfile="%jarFolderPath%\common\log4j-1.2.15.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
@REM report-web中需要的jar
call mvn deploy:deploy-file -DgroupId=com.lowagie -DartifactId=iTextAsian -Dversion=1.0 -Dfile="%jarFolderPath%\common\iTextAsian-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=com.lowagie -DartifactId=itext -Dversion=2.1.0 -Dfile="%jarFolderPath%\common\iText-2.1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=com.lowagie -DartifactId=iTextAsianCmaps -Dversion=1.0 -Dfile="%jarFolderPath%\common\iTextAsianCmaps-1.0.jar" -Dpackaging=jar -Durl="file://%folderPath%\"


@REM IPMS jibx jar
call mvn deploy:deploy-file -DgroupId=org.apache.bcel -DartifactId=bcel -Dversion=5.1 -Dfile="%jarFolderPath%\jibx\bcel-5.1.jar" -Dpackaging=jar -Durl="file://%folderPath%\"
call mvn deploy:deploy-file -DgroupId=xmlpull -DartifactId=xmlpull -Dversion=1.1.4 -Dfile="%jarFolderPath%\jibx\xmlpull-1.1.4.jar" -Dpackaging=jar -Durl="file://%folderPath%\"


call mvn deploy:deploy-file -DgroupId=it.sauronsoftware -DartifactId=ftp4j -Dversion=1.6 -Dfile="%jarFolderPath%\it\ftp4j-1.6.jar" -Dpackaging=jar -Durl="file://%folderPath%\"

cmd

