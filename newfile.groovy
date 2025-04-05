
pipeline {
    agent any
        tools{
            maven 'maven-3.9.8'
        }
    stages {

        stage('pull code'){

            steps{
                git branch: 'main', url: 'https://github.com/Garima1973/newclientapp.git'
            }
        }

        stage('build'){

            steps{
                sh 'mvn clean install'
            }
        }

        //  stage('Restart Nginx'){

        //     steps{
        //         sshagent(['frontend']) {
        //             sh 'ssh -o StrictHostKeyChecking=no ubuntu@13.234.66.239 "sudo systemctl restart nginx"'

        //             }
        //     }
        // }
    }
}