# Sử dụng JDK 21 làm base image cho build stage
FROM eclipse-temurin:21-alpine-3.21 AS build

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép gradle files
COPY gradle/ gradle/
COPY build.gradle settings.gradle gradlew ./

# Sao chép source code
COPY src/ src/

# Build ứng dụng
RUN ./gradlew bootJar --no-daemon

# Sử dụng JRE 21 cho runtime (nhẹ hơn JDK)
FROM eclipse-temurin:21-jre-alpine-3.21

# Thiết lập thông tin về người bảo trì
LABEL maintainer="dwnq.coding@gmail.com"
LABEL description="PureSound API - Music Streaming Backend Service"
LABEL version="1.0.1"

# Tạo thư mục logs và config
RUN mkdir -p /app/logs /app/config

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép JAR file từ build stage
COPY --from=build /app/build/libs/*.jar puresound-api.jar

# Thiết lập biến môi trường
ENV SPRING_PROFILES_ACTIVE=prod
ENV TZ=Asia/Ho_Chi_Minh

# Expose port 8080
EXPOSE 8080

# Chạy ứng dụng với các tham số JVM tối ưu cho JDK 21
ENTRYPOINT ["java", "--enable-preview", "-jar", "puresound-api.jar"]
