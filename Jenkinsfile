pipeline {
  agent any
  
  stages {
    stage('Checkout') {
      checkout scm
    }
    stage('Build') {
      steps {
        echo 'Building...'
        sh 'gradle clean build'
      }
    }
    stage('Test') {
      steps {
        echo 'Testing...'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying..'
      }
    }
  }
}
