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
                # sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=$SONAR_LOGIN -Dsonar.projectKey=kcl'
                sh 'mvn clean verify -Psonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=$SONAR_LOGIN -Dsonar.projectKey=kcl'
            }
        }
    }
}

