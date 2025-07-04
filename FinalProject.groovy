pipeline {
    agent any

    stages {

        stage('pull code'){

            steps{
                git branch: 'master', url: 'https://github.com/Garima1973/FinalProjectFile.git'
            }
        }
        stage('sending files to docker server')
        {
            steps{
                  sshagent(['docker_server'])
                   {
                sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@3.107.209.210:/home/ubuntu/FinalProject'
                // public ip of docker because docker is the destination
                  }
            }
        }
        stage('docker image')
        {
            steps{
                sshagent(['docker_server'])
                {
                     sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.107.209.210 "docker build -t garima3201/finalprojectdockerjenkins /home/ubuntu/FinalProject"'
                }
            }
        }



        stage('Build Docker Image on Remote Server') {
            steps {
                sshagent(['docker_server']) {
                    
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.107.209.210 "docker image build -t finalprojectdockerjenkins:v1.$BUILD_ID /home/ubuntu/FinalProject"'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.107.209.210 "docker image tag finalprojectdockerjenkins:v1.$BUILD_ID garima3201/finalprojectdockerjenkins:v1.$BUILD_ID"'
                }
            }
        }

        stage('Push Image to DockerHub') {
            steps {
                withCredentials([string(credentialsId: 'Dockerpassid', variable: 'dockerpass')]) {
                    sshagent(['docker_server']) {
                        sh '''
                            ssh -o StrictHostKeyChecking=no ubuntu@3.107.209.210 "
                            docker login -u garima3201 -p ${dockerpass} &&
                            docker push garima3201/finalprojectdockerjenkins:v1.$BUILD_ID &&
                            docker push garima3201/finalprojectdockerjenkins:latest"
                        '''
                    }
                }
            }
        }

          

    }
 }