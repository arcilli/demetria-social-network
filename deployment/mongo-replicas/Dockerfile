FROM mongo
COPY init.sh /usr/local/
COPY mongodump-test-db /usr/local/
RUN chmod +x /usr/local/init.sh
CMD /usr/local/init.sh