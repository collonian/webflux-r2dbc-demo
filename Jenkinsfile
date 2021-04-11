pipeline {
  agent any
  
  stages {
    stage('Dockerbuild') {
      steps {
        checkout scm
        docker.build('webflux-r2dbc-demo:${env.BUILD-ID}')
      }
    }
  }
}
