pipeline {
    agent any

    tools {
        maven 'Maven-3.9.9'
        jdk 'JDK-17'
        gradle 'Gradle-7.2'
        go 'Go-1.23'
    }

    environment {
        RELEASE_VERSION = "1.0.0"
        IMAGE_TAG  "${RELEASE_VERSION}-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Cleanup workspace') {
            steps {
                cleanWs()
            }
        }
    }
}