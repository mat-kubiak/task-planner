#
# Task Planner
# Copyright (C) 2024 Mateusz Kubiak
# 
# Licensed under the GNU General Public License v3. 
# See LICENSE or visit <https://www.gnu.org/licenses/>.
#

FROM maven:3.8.5-openjdk-17

ENV HOME=/home/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD pom.xml $HOME
RUN mvn verify clean --fail-never

ADD . $HOME
# TODO: enable tests
RUN mvn package -DskipTests

EXPOSE 8080
CMD mvn spring-boot:run