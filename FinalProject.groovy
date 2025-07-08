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
                sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@54.252.228.164:/home/ubuntu/FinalProject'
                // public ip of docker because docker is the destination
                  }
            }
        }
        stage('docker image')
        {
            steps{
                sshagent(['docker_server'])
                {
                     sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.228.164 "docker build -t garima3201/finalprojectdockerjenkins /home/ubuntu/FinalProject"'
                }
            }
        }



        stage('Build Docker Image on Remote Server') {
            steps {
                sshagent(['docker_server']) {
                    
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.228.164 "docker image build -t finalprojectdockerjenkins:v1.$BUILD_ID /home/ubuntu/FinalProject"'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@54.252.228.164 "docker image tag finalprojectdockerjenkins:v1.$BUILD_ID garima3201/finalprojectdockerjenkins:v1.$BUILD_ID"'
                }
            }
        }

        stage('Push Image to DockerHub') {
            steps {
                withCredentials([string(credentialsId: 'Dockerpassid', variable: 'dockerpass')]) {
                    sshagent(['docker_server']) {
                        sh '''
                            ssh -o StrictHostKeyChecking=no ubuntu@54.252.228.164 "
                            docker login -u garima3201 -p ${dockerpass} &&
                            docker push garima3201/finalprojectdockerjenkins:v1.$BUILD_ID &&
                            docker push garima3201/finalprojectdockerjenkins:latest"
                        '''
                    }
                }
            }
        }

         stage('Kubernetes Execution') {
            steps {
                sshagent(['KubID']) {
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.25.110.19 rm -r /home/ubuntu/templates'
                    sh 'scp -r -o StrictHostKeyChecking=no templates/ ubuntu@3.25.110.19:/home/ubuntu/'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.25.110.19 kubectl delete -f /home/ubuntu/templates/'
                    sh 'sleep 5 && echo "Waiting for pods to terminate"'
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@3.25.110.19 kubectl apply -f /home/ubuntu/templates/'
                }
            }
        } 

    }
 }