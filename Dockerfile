FROM ubuntu:20.04

LABEL author="grishan0v"
LABEL maintainer="grishanov.dev@gmail.com"
LABEL version="1.0"
LABEL description="Otus Homework: Docker image for Jenkins with Android SDK"

# неободимо установить временную зону чтобы Дженкинс показывал локальное время
ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# добавляем i386 архитектуру для установки ia32-libs, требуемые для сборки проектов на Android SDK
RUN dpkg --add-architecture i386

# обновляем пакеты и устанавливаем нужное
RUN apt-get update && apt-get install -y\
 git wget unzip sudo mc tzdata locales openjdk-11-jdk libncurses5:i386 libstdc++6:i386 zlib1g:i386 net-tools nano curl\
 && apt-get clean\
 && rm -rf /var/lib/apt/lists /var/cache/apt

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

# загрузка и распаковка Android SDK в необходимую папку
ARG android_home_dir=/var/lib/android-sdk/
ARG commandlinetools_zip_file=commandlinetools-linux-7583922_latest.zip
RUN mkdir $android_home_dir
RUN wget https://dl.google.com/android/repository/$commandlinetools_zip_file -P $android_home_dir -nv
RUN unzip $android_home_dir$commandlinetools_zip_file -d $android_home_dir/cmdline-tools
RUN rm $android_home_dir$commandlinetools_zip_file\
 && chmod 777 -R $android_home_dir\
 && mv $android_home_dir/cmdline-tools/cmdline-tools/ $android_home_dir/cmdline-tools/tools/

# устанавливаем environment в наш образ
ENV ANDROID_SDK_ROOT=$android_home_dir
ENV PATH="${PATH}:$android_home_dir/cmdline-tools/tools/bin:$android_home_dir/platform-tools"
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/

# соглашаемся с лицензионным соглашением Android SDK
RUN yes | sdkmanager --licenses && sdkmanager "platform-tools"

# устанавливаем домашнюю директорию jenkins
ENV JENKINS_HOME=/var/lib/jenkins
RUN mkdir $JENKINS_HOME && chmod 777 $JENKINS_HOME

# добавляем пользователя с именем 'jenkins' т.к. пользоваться пользователем 'root' не кашерно
RUN useradd -m jenkins && echo 'jenkins ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
USER jenkins
WORKDIR /home/jenkins

# создаем рабочую директорию для Jenkins
RUN sudo chmod 777 -R "$android_home_dir/"

# генерируем adb ключ
RUN mkdir .android && adb keygen .android/adbkey

# загрузим и запустим war файл с последней версией Jenkins
RUN wget http://mirrors.jenkins.io/war-stable/latest/jenkins.war -nv
CMD java -jar jenkins.war

EXPOSE 8080/tcp