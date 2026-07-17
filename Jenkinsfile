pipeline {
    agent any

    environment {
        VPS_USER   = 'root'
        VPS_HOST   = '103.77.242.65'
        VPS_DIR    = '/opt/RestAPI-Cinema'
        SSH_KEY_ID = 'ssh-key'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Hughgz/RestAPI-Cinema.git',
                    credentialsId: 'ssh-key'
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
                withCredentials([sshUserPrivateKey(credentialsId: "${SSH_KEY_ID}", keyFileVariable: 'SSH_KEY')]) {
                    bat """
                        scp -i %SSH_KEY% -o StrictHostKeyChecking=no target/API-Cinema-0.0.1-SNAPSHOT.jar Dockerfile.deploy docker-compose.yml ${VPS_USER}@${VPS_HOST}:${VPS_DIR}/
                    """
                }
            }
        }

        stage('Deploy on VPS') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: "${SSH_KEY_ID}", keyFileVariable: 'SSH_KEY')]) {
                    bat """
                        ssh -i %SSH_KEY% -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} "cd ${VPS_DIR} && cp Dockerfile.deploy Dockerfile && docker-compose down && docker-compose build && docker-compose up -d"
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
