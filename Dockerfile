# Use WildFly as the base image
FROM quay.io/wildfly/wildfly:27.0.1.Final

# Add the Derby database JAR to the WildFly modules directory
ADD https://repo1.maven.org/maven2/org/apache/derby/derby/10.17.1.0/derby-10.17.1.0.jar /opt/jboss/wildfly/standalone/lib/

# Copy the application WAR file into deployments
COPY target/EAStudent.war /opt/jboss/wildfly/standalone/deployments/EAStudent.war

# Expose WildFly HTTP port
EXPOSE 8080

# Optional: Start WildFly in standalone mode with custom configuration if needed
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]