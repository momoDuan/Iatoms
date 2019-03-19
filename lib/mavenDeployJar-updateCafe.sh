#REM cafe-framework jar
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-cm -Dversion=1.0 -Dfile="./cafe/cafe-cm-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-core -Dversion=1.0 -Dfile="./cafe/cafe-core-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-identity -Dversion=1.0 -Dfile="./cafe/cafe-identity-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-workflow -Dversion=1.0 -Dfile="./cafe/cafe-workflow-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
