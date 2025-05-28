pipeline {
    agent any

    stages {

        stage('pull code'){

            steps{
                git branch: 'main', url: 'https://github.com/Garima1973/frontend-docker-cicd.git'
            }
        }
        stage('sending files to docker server')
        {
            steps{
                  sshagent(['docker_server'])
                   {
                sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@52.62.1.66:/home/ubuntu/frontenddocker/'
                // public ip of docker because docker is the destination
                  }
            }
        }

    }
 }