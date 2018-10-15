FROM maven:3.3.9-jdk-8

LABEL Description="Bears checkbranch docker image" Vendor="Spirals" Version="0.0.0"

RUN mkdir -p /var/app/pr/

COPY . /var/app/

RUN echo "Europe/Paris" > /etc/timezone && chmod +x /var/app/*.sh

ENV M2_HOME=$MAVEN_HOME

ENV BRANCH_NAME=
ENV IS_BUGGY_COMMIT=

WORKDIR /var/app/
ENTRYPOINT /var/app/check_with_maven.sh $BRANCH_NAME $IS_BUGGY_COMMIT