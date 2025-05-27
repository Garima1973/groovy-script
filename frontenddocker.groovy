pipeline {
    agent any

    stages {

        stage('pull code'){

            steps{
                git branch: 'main', url: 'https://github.com/Garima1973/frontend-docker-cicd.git'
            }
        }

    }
 }