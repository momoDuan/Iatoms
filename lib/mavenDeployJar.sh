#REM  secutity
mvn deploy:deploy-file -DgroupId=com.cybersoft4u.framework -DartifactId=cyber-iatoms-security -Dversion=1.0 -Dfile="./security/cyber-iatoms-security-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.owasp -DartifactId=antisamy -Dversion=1.4.3 -Dfile="./security/antisamy-1.4.3.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.owasp -DartifactId=ESAPI -Dversion=2.0.1 -Dfile="./security/esapi-2.0.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
#REM cafe-framework jar
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-cm -Dversion=1.0 -Dfile="./cafe/cafe-cm-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-core -Dversion=1.0 -Dfile="./cafe/cafe-core-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-identity -Dversion=1.0 -Dfile="./cafe/cafe-identity-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=cafe-framework -DartifactId=cafe-workflow -Dversion=1.0 -Dfile="./cafe/cafe-workflow-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.ibm.mq -DartifactId=com.ibm.mq -Dversion=5.2 -Dfile="./mq/com.ibm.mq-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.ibm.mq -DartifactId=com.ibm.mq.fta -Dversion=5.2 -Dfile="./mq/com.ibm.mq.fta-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.ibm.mqetclient -DartifactId=com.ibm.mqetclient -Dversion=5.2 -Dfile="./mq/com.ibm.mqetclient-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.ibm.mqjms -DartifactId=com.ibm.mqjms -Dversion=5.2 -Dfile="./mq/com.ibm.mqjms-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.ibm.mq -DartifactId=commonservices -Dversion=5.2 -Dfile="./mq/commonservices-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.ibm.mq -DartifactId=dhbcore -Dversion=5.2 -Dfile="./mq/dhbcore-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=rmm -DartifactId=rmm -Dversion=5.2 -Dfile="./mq/rmm-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.sun.jndi -DartifactId=fscontext -Dversion=5.2 -Dfile="./mq/fscontext-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.sun.jndi -DartifactId=providerutil -Dversion=5.2 -Dfile="./mq/providerutil-5.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=jazn -Dversion=10.1.3.4 -Dfile="./oracle/jazn-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=jazncore -Dversion=10.1.3.4 -Dfile="./oracle/jazncore-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-orawsdl -Dversion=10.1.3.4 -Dfile="./oracle/oracle-orawsdl-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.ucm -DartifactId=oracle-ucm -Dversion=10.1.3 -Dfile="./oracle/oracle-ucm-10.1.3.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-xml -Dversion=10.1.3.4 -Dfile="./oracle/oracle-xml-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-xmlparserv2 -Dversion=10.1.3.4 -Dfile="./oracle/oracle-xmlparserv2-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=oracle -DartifactId=jdbc -Dversion=10.2.0.1 -Dfile="./oracle/jdbc-10.2.0.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-orabpel-thirdparty -Dversion=10.1.3.4 -Dfile="./oracle/oracle-orabpel-thirdparty-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-bc4j -Dversion=10.1.3.4 -Dfile="./oracle/oracle-bc4j-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-bicmn -Dversion=10.1.3.4 -Dfile="./oracle/oracle-bicmn-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-bipres -Dversion=10.1.3.4 -Dfile="./oracle/oracle-bipres-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-bpm-infra -Dversion=10.1.3.4 -Dfile="./oracle/oracle-bpm-infra-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-bpm-services -Dversion=10.1.3.4 -Dfile="./oracle/oracle-bpm-services-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-oc4j-internal -Dversion=10.1.3.4 -Dfile="./oracle/oracle-oc4j-internal-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-oc4jclient -Dversion=10.1.3.4 -Dfile="./oracle/oracle-oc4jclient-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-optic -Dversion=10.1.3.4 -Dfile="./oracle/oracle-optic-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-orabpel -Dversion=10.1.3.4 -Dfile="./oracle/oracle-orabpel-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-orabpel-common -Dversion=10.1.3.4 -Dfile="./oracle/oracle-orabpel-common-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-orabpel-exts -Dversion=10.1.3.4 -Dfile="./oracle/oracle-orabpel-exts-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-orasaaj -Dversion=10.1.3.4 -Dfile="./oracle/oracle-orasaaj-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=oracle-soap -Dversion=10.1.3.4 -Dfile="./oracle/oracle-soap-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=ora.soa -DartifactId=wsclient_extended -Dversion=10.1.3.4 -Dfile="./oracle/wsclient_extended-10.1.3.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=webserviceclient -DartifactId=webserviceclient -Dversion=10.3.0 -Dfile="./oracle/webserviceclient-10.3.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=j2ee -DartifactId=j2ee -Dversion=1.4.1 -Dfile="./j2ee/j2ee-1.4.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=dwr -DartifactId=dwr -Dversion=3.0.0 -Dfile="./common/dwr-3.0.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.caucho -DartifactId=hessian -Dversion=3.1.6 -Dfile="./common/hessian-3.1.6.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=sun.jai -DartifactId=jai_codec -Dversion=1.1.2 -Dfile="./common/jai_codec-1.1.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=sun.jai -DartifactId=jai_core -Dversion=1.1.2 -Dfile="./common/jai_core-1.1.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=toplink -DartifactId=toplink -Dversion=1.0 -Dfile="./common/toplink-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=javax.xml -DartifactId=jaxrpc-api -Dversion=1.4.2 -Dfile="./common/jaxrpc-api-1.4.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=c3p0 -DartifactId=c3p0 -Dversion=0.9.1.2 -Dfile="./common/c3p0-0.9.1.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=javax.resource -DartifactId=connector-api -Dversion=1.5 -Dfile="./j2ee/connector-api-1.5.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=javassist -DartifactId=javassist -Dversion=3.8.0.GA -Dfile="./common/javassist-3.8.0.GA.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=jdsl -DartifactId=jdsl -Dversion=2.1.1 -Dfile="./common/jdsl-2.1.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=xml-apis -DartifactId=xml-apis -Dversion=1.3.04 -Dfile="./common/xml-apis-1.3.04.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=xerces -DartifactId=xerces -Dversion=2.4.0 -Dfile="./common/xerces-2.4.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.ant -DartifactId=ant-launcher -Dversion=1.7.1 -Dfile="./common/ant-launcher-1.7.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.ant -DartifactId=ant -Dversion=1.7.1 -Dfile="./common/ant-1.7.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=log4j -DartifactId=log4j -Dversion=1.2.15 -Dfile="./common/log4j-1.2.15.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.codehaus.woodstox -DartifactId=wstx-asl -Dversion=3.2.7 -Dfile="./common/wstx-asl-3.2.7.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=net.sf.jasperreports -DartifactId=jasperreports -Dversion=4.5.0 -Dfile="./common/jasperreports-4.5.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.lowagie -DartifactId=itext -Dversion=2.1.7 -Dfile="./common/iText-2.1.7.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=eclipse -DartifactId=jdtcore -Dversion=3.1.0 -Dfile="./common/jdtcore-3.1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=jfree -DartifactId=jfreechart -Dversion=1.0.12 -Dfile="./common/jfreechart-1.0.12.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.codehaus.castor -DartifactId=castor -Dversion=1.2 -Dfile="./common/castor-1.2.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=commons-beanutils -DartifactId=commons-beanutils -Dversion=1.8.0 -Dfile="./common/commons-beanutils-1.8.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=commons-digester -DartifactId=commons-digester -Dversion=1.7 -Dfile="./common/commons-digester-1.7.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.bcel -DartifactId=bcel -Dversion=5.1 -Dfile="./jibx/bcel-5.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=xmlpull -DartifactId=xmlpull -Dversion=1.1.4 -Dfile="./jibx/xmlpull-1.1.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.springframework -DartifactId=spring-mock -Dversion=2.0.8 -Dfile="./spring/spring-mock-2.0.8.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.springframework.asm -DartifactId=org.springframework.asm -Dversion=3.0.0.M3 -Dfile="./spring/org.springframework.asm-3.0.0.M3.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.springframework.expression -DartifactId=org.springframework.expression -Dversion=3.0.0.M3 -Dfile="./spring/org.springframework.expression-3.0.0.M3.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.springframework.ws -DartifactId=spring-ws -Dversion=1.5.6-all -Dfile="./spring/spring-ws-1.5.6-all.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=commons-codec -DartifactId=commons-codec -Dversion=1.3 -Dfile="./common/commons-codec-1.3.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.lowagie -DartifactId=iTextAsian -Dversion=1.0 -Dfile="./common/iTextAsian-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.lowagie -DartifactId=itext -Dversion=2.1.0 -Dfile="./common/iText-2.1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.lowagie -DartifactId=iTextAsianCmaps -Dversion=1.0 -Dfile="./common/iTextAsianCmaps-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=daisydiff -DartifactId=daisydiff -Dversion=1.0 -Dfile="./daisydiff/daisydiff-1.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.ws.commons.axiom -DartifactId=axiom-api -Dversion=1.2.8 -Dfile="./axis2/axiom-api-1.2.8.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.ws.commons.axiom -DartifactId=axiom-impl -Dversion=1.2.8 -Dfile="./axis2/axiom-impl-1.2.8.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.ws.commons.axiom -DartifactId=axiom-dom -Dversion=1.2.8 -Dfile="./axis2/axiom-dom-1.2.8.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.axis2 -DartifactId=axis2-adb -Dversion=1.5 -Dfile="./axis2/axis2-adb-1.5.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.axis2 -DartifactId=axis2-kernel -Dversion=1.5 -Dfile="./axis2/axis2-kernel-1.5.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.axis2 -DartifactId=axis2-transport-http -Dversion=1.5 -Dfile="./axis2/axis2-transport-http-1.5.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.axis2 -DartifactId=axis2-transport-local -Dversion=1.5 -Dfile="./axis2/axis2-transport-local-1.5.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.axis2 -DartifactId=axis2-transport-tcp -Dversion=1.0.0 -Dfile="./axis2/axis2-transport-tcp-1.0.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.neethi -DartifactId=neethi -Dversion=2.0.4 -Dfile="./axis2/neethi-2.0.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=commons-httpclient -DartifactId=httpcore -Dversion=4.0 -Dfile="./axis2/httpcore-4.0.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.neethi -DartifactId=neethi -Dversion=2.0.4 -Dfile="./axis2/neethi-2.0.4.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.sun.jna -DartifactId=jna -Dversion=3.5.1 -Dfile="./sun/jna-3.5.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com.sun.jna.platform -DartifactId=platform -Dversion=3.5.1 -Dfile="./sun/platform-3.5.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.apache.maven.plugins -DartifactId=maven-dependency-plugin -Dversion=2.8 -Dfile="./maven/maven-dependency-plugin-2.8.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=org.codehaus.plexus -DartifactId=plexus-utils -Dversion=3.0.5 -Dfile="./org/plexus-utils-3.0.5.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"
mvn deploy:deploy-file -DgroupId=com-google-gson -DartifactId=gson -Dversion=2.3.1 -Dfile="./json/gson-2.3.1.jar" -Dpackaging=jar -Durl="file:///home/cybersoft/.m2/repository/"

