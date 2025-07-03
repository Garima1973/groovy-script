pipeline {
    agent any

    stages {

        stage('pull code'){

            steps{
                git branch: 'main', url: 'https://github.com/Garima1973/FinalProjectFile.git'
            }
        }
        stage('sending files to docker server')
        {
            steps{
                  sshagent(['docker_server'])
                   {
                sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@13.239.37.205:/home/ubuntu/FinalProject'
                // public ip of docker because docker is the destination
                  }
            }
        }
        stage('docker image')
        {
            steps{
                sshagent(['docker_server'])
                {
                     sh 'ssh -o StrictHostKeyChecking=no ubuntu@13.239.37.205 "docker build -t finalprojectdockerjenkins /home/ubuntu/FinalProject"'
                }
            }
        }

        // stage('docker run')
        // {
        //     steps{
        //         sshagent(['docker_server'])
        //         {   
        //            sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.71.148 "docker stop backenddockerjenkins"' 

        //            sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.71.148 "docker rm backenddockerjenkins"' 

 
        //            sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.71.148 "docker run -d -p 8080:8080 --name backenddockerjenkins --network my-network  backenddockerjenkins"' 
        //         }
        //     }
        // }

          

    }
 }