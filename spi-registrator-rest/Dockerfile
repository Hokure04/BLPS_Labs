FROM bitnami/keycloak:26.2.0

COPY build/libs/spi-registrator-rest-1.0-SNAPSHOT.jar /opt/bitnami/keycloak/providers/spi-registrator-rest-1.0-SNAPSHOT.jar

ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin

ENTRYPOINT ["/opt/bitnami/scripts/keycloak/entrypoint.sh"]
CMD ["/opt/bitnami/scripts/keycloak/run.sh"]