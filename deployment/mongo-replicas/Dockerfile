FROM mongo
COPY init.sh /usr/local/
COPY mongo-initial-load.js /usr/local
RUN chmod +x /usr/local/init.sh
CMD /usr/local/init.sh