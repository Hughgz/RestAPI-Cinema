pipeline {
    agent any

    environment {
        VPS_USER   = 'root'
        VPS_HOST   = '103.77.242.65'
        VPS_DIR    = '/opt/RestAPI-Cinema'
        SSH_KEY_ID = 'SHA256:8IR2vSFD0Y53cqoy7glfkZepbUnxxO0bILczvEIsPQs'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Hughgz/RestAPI-Cinema.git',
                    credentialsId: 'SHA256:wgxEMTwwa1C0/foq6+Q8b1tBlIr6XQy9lGI8eHtpO58'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvnw.cmd clean test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Transfer to VPS') {
            steps {
                sshagent(["${SSH_KEY_ID}"]) {
                    bat """
                        scp -o StrictHostKeyChecking=no -r pom.xml mvnw.cmd .mvn src Dockerfile docker-compose.yml ${VPS_USER}@${VPS_HOST}:${VPS_DIR}/
                    """
                }
            }
        }

        stage('Build & Deploy on VPS') {
            steps {
                sshagent(["${SSH_KEY_ID}"]) {
                    bat """
                        ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} "cd ${VPS_DIR} && docker-compose down && docker-compose build --no-cache && docker-compose up -d"
                    """
                }
            }
        }
    }

    post {
        always {
            bat 'mvnw.cmd clean'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
