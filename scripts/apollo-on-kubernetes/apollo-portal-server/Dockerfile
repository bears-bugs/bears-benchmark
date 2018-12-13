# Dockerfile for apollo-portal-server

# Build with:
# docker build -t apollo-portal-server:v1.0.0 .

FROM openjdk:8-jre-alpine3.8

RUN \
    echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories && \
    echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories  && \
    apk update upgrade && \
    apk add --no-cache procps curl bash tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    mkdir -p /apollo-portal-server

ADD . /apollo-portal-server/

ENV APOLLO_PORTAL_SERVICE_NAME="service-apollo-portal-server.sre"

ENV DATASOURCES_URL="jdbc:mysql://service-mysql-for-apollo.sre:3306/ApolloPortalDB?characterEncoding=utf8"
ENV DATASOURCES_USERNAME="FillInCorrectUser"
ENV DATASOURCES_PASSWORD="FillInCorrectPassword"

ENV DEV_META_SERVICE_NAME="service-apollo-config-server-dev.sre"
ENV TEST_ALPHA_META_SERVICE_NAME="service-apollo-config-server-test-alpha.sre"
ENV TEST_BETA_META_SERVICE_NAME="service-apollo-config-server-test-beta.sre"
ENV PROD_META_SERVICE_NAME="service-apollo-config-server-prod.sre"

EXPOSE 8070

ENTRYPOINT ["/apollo-portal-server/entrypoint.sh"]

CMD ["/apollo-portal-server/scripts/startup-kubernetes.sh"]
