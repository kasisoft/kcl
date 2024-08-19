pipeline {
    agent any
    tools {
        maven 'maven-3.9.8'
        jdk 'java-21'
    }
    environment {
        USER = 'kasimir'
    }
    stages {
        stage('Initialize') {
            steps {
                sh 'echo "PATH = ${PATH}"'
                sh 'echo "M2_HOME = ${M2_HOME}"'
                sh 'echo "USER = ${USER}"'
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
            withSonarQubeEnv() {
                sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=kcl'
            }
        }
    }
}

