:: запуск микросервисов инфраструктуры

start "discovery-server" java -jar discovery-server/target/discovery-server-1.0-SNAPSHOT.jar
timeout 5
start "config-server" java -jar config-server/target/config-server-1.0-SNAPSHOT.jar