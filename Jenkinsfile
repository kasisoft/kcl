pipeline {
    agent any
    tools {
        maven 'maven-3.9.8'
        jdk 'java-21'
    }
    environment {
        USER = 'kasimir'
        SONAR_URL = credentials('sonar.url')
        SONAR_LOGIN = credentials('sonar.login')
    }
    stages {
        stage('Initialize') {
            steps {
                sh 'echo "PATH = ${PATH}"'
                sh 'echo "M2_HOME = ${M2_HOME}"'
                sh 'echo "USER = ${USER}"'
                sh 'echo "MAVEN_OPTS = ${MAVEN_OPTS}"'
                sh 'echo "SONAR_URL = ${SONAR_URL}"'
                sh 'echo "SONAR_LOGIN = ${SONAR_LOGIN}"'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean install'
            }
        }
        stage('Testing') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar -Dsonar.host.url=https://arturius.kasisoft.com/sonar -Dsonar.login=$SONAR_LOGIN -Dsonar.projectKey=kcl'
            }
        }
    }
}

