pipeline {
    agent any

    stages {

        stage('pull code'){

            steps{
                git branch: 'main', url: 'https://github.com/Garima1973/front-end-application.git'
            }
        }

        stage('deploy'){

            steps{
                sshagent(['frontend']) {
                    sh 'ssh -o StrictHostKeyChecking=no ubuntu@13.234.66.239 "sudo rm -r /var/www/html/*"'
                    sh 'scp -o StrictHostKeyChecking=no -r * ubuntu@13.234.66.239:/home/ubuntu/frontend/'
                    }
            }
        }
    }
}