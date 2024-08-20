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
                // properties such as sonar.host.url and sonar.login are configured in the settings.xml for this profile
                sh 'mvn clean verify -Psonar -Dsonar.projectKey=kcl'
            }
        }
    }
}

