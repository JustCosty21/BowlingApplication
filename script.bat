call mvn clean compile install
call cd bowling
call mvn clean package spring-boot:repackage
call cd ..
call docker build -t bowlingapp .
call docker run -dp 8080:8080 bowlingapp