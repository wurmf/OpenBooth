# Ubuntu 18.04
# Oracle Java 1.8.0_172 64 bit
# Maven 3.5.3

FROM ubuntu:18.04

MAINTAINER Martin Schröder (https://github.com/schrer)

# this is a non-interactive automated build - avoid some warning messages
ENV DEBIAN_FRONTEND noninteractive

# update dpkg repositories
RUN apt-get update 

# install wget
RUN apt-get install -y wget

# set shell variable for maven installation
ENV mvnversion 3.5.3
ENV mvnname apache-maven-$mvnversion

# get maven
RUN wget --no-verbose -O /tmp/$mvnname.tar.gz http://archive.apache.org/dist/maven/maven-3/3.5.3/binaries/$mvnname-bin.tar.gz

# verify checksum
RUN echo "51025855d5a7456fc1a67666fbef29de /tmp/$mvnname.tar.gz" | md5sum -c

# install maven
RUN tar xzf /tmp/$mvnname.tar.gz -C /opt/
RUN ln -s /opt/$mvnname /opt/maven
RUN ln -s /opt/maven/bin/mvn /usr/local/bin
RUN rm -f /tmp/$mvnname.tar.gz
ENV MAVEN_HOME /opt/maven

# remove download archive files
RUN apt-get clean

# set shell variables for java installation
ENV java_version 1.8.0_172
ENV filename jdk-8u172-linux-x64.tar.gz
ENV downloadlink http://download.oracle.com/otn-pub/java/jdk/8u172-b11/a58eab1ec242421181065cdc37240b08/$filename

# download java, accepting the license agreement
RUN wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" -O /tmp/$filename $downloadlink 

# unpack java
RUN mkdir /opt/java-oracle && tar -zxf /tmp/$filename -C /opt/java-oracle/
ENV JAVA_HOME /opt/java-oracle/jdk$java_version
ENV PATH $JAVA_HOME/bin:$PATH

# configure symbolic links for the java and javac executables
RUN update-alternatives --install /usr/bin/java java $JAVA_HOME/bin/java 20000 && update-alternatives --install /usr/bin/javac javac $JAVA_HOME/bin/javac 20000

CMD [""]


