FROM mongo
COPY init.sh /usr/local/
ADD dump /usr/local/dump
RUN chmod +x /usr/local/init.sh
CMD /usr/local/init.sh
