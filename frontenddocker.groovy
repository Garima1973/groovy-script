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
                sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@3.24.136.56:/home/ubuntu/frontenddocker/'
                // public ip of docker because docker is the destination
                  }
            }
        }
        stage('docker image')
        {
            steps{
                sshagent(['docker_server'])
                {
                     sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.24.136.56 "docker build -t frontenddockerjenkins /home/ubuntu/frontenddocker"'
                }
            }
        }

        stage('docker run')
        {
            steps{
                sshagent(['docker_server'])
                {
                   sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.24.136.56 "docker run -d -p 5007:80 frontenddockerjenkins"' 
                }
            }
        }

    }
 }