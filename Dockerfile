FROM clojure
ADD ./target/uberjar/micropress-standalone.jar ./
EXPOSE 3001
CMD [ "java", "-jar", "./micropress-standalone.jar" ]
