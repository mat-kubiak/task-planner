#
# Task Planner
# Copyright (C) 2024 Mateusz Kubiak
# 
# Licensed under the GNU General Public License v3. 
# See LICENSE or visit <https://www.gnu.org/licenses/>.
#

FROM maven:3.9-amazoncorretto-17-alpine AS build 
WORKDIR /home/usr/app

COPY pom.xml .
RUN mvn dependency:resolve dependency:resolve-plugins

COPY src src
RUN mvn package -DskipTests

FROM amazoncorretto:17-alpine

WORKDIR /home/usr/app
COPY --from=build /home/usr/app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
