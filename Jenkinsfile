pipeline {
    agent any
    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk21'
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
    }
}

