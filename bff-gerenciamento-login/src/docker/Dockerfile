FROM fabric8/java-centos-openjdk11-jdk:1.8.0

ENV AB_OFF=true \
    TZ=America/Sao_Paulo \
    SPRING_PROFILES_ACTIVE="docker" \
    JAVA_MAX_MEM_RATIO="0" \
    JAVA_MAJOR_VERSION="8" \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF8"

ADD @project.artifactId@-@project.version@.jar $JAVA_APP_DIR/