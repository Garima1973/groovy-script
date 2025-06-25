pipeline {
    agent any

    stages {

        stage('pull code'){

            steps{
                git branch: 'main', url: 'https://github.com/Garima1973/For-Backend-Docker.git'
            }
        }
        stage('sending files to docker server')
        {
            steps{
                  sshagent(['docker_server'])
                   {
                sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@54.252.71.148:/home/ubuntu/backendocker/'
                // public ip of docker because docker is the destination
                  }
            }
        }
        stage('docker image')
        {
            steps{
                sshagent(['docker_server'])
                {
                     sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.71.148 "docker build -t backenddockerjenkins /home/ubuntu/backendocker"'
                }
            }
        }

        stage('docker run')
        {
            steps{
                sshagent(['docker_server'])
                {   
                   sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.71.148 "docker stop backenddockerjenkins"' 

                   sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.71.148 "docker rm backenddockerjenkins"' 

 
                   sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.71.148 "docker run -d -p 8080:8080 --name backenddockerjenkins --network my-network  backenddockerjenkins"' 
                }
            }
        }

          

    }
 }