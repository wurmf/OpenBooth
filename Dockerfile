FROM ubuntu:18.04


RUN \
  apt-get update && \
  apt-get install software-properties-common -y && \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer && \
  export JAVA_HOME=/usr/lib/jvm/java-8-oracle
  apt-get install -y maven

CMD bash
