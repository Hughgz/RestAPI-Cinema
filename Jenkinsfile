pipeline {
    agent any

    environment {
        VPS_DIR = '/opt/RestAPI-Cinema'
    }

    stages {
        stage('Checkout') {
            steps {
                dir("${VPS_DIR}") {
                    git branch: 'master',
                        url: 'https://github.com/Hughgz/RestAPI-Cinema.git',
                        credentialsId: 'github-cred'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir("${VPS_DIR}") {
                    sh 'docker-compose build'
                }
            }
        }

        stage('Deploy') {
            steps {
                dir("${VPS_DIR}") {
                    sh 'docker-compose down'
                    sh 'docker-compose up -d'
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh 'docker image prune -f'
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
