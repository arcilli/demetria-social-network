FROM orientdb:latest
LABEL MANTAINER="Gabriel Raileanu mail.raileanu [at] gmail [dot] com"
RUN apt-get update && apt-get -y install netcat && apt-get clean
COPY ./orient-setup.sh /usr/local/
COPY ./initial-db.gz /usr/local/
WORKDIR /usr/local/
RUN chmod u+x orient-setup.sh
ENTRYPOINT ["bash", "orient-setup.sh"]