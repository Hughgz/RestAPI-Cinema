pipeline {
    agent any

    environment {
        DOCKER_IMAGE    = 'minhhieu1108/cinema-api'
        DOCKER_TAG      = "${BUILD_NUMBER}"
        COMPOSE_PROJECT = 'cinema'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/your-user/RestAPI-Cinema.git',
                    credentialsId: 'github-credentials'
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

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push('latest')
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['ssh-deploy-key']) {
                    bat '''
                        ssh root@103.77.242.65 "cd /opt/RestAPI-Cinema/ && docker-compose pull && docker-compose up -d"
                    '''
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
