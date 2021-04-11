pipeline {
  agent { dockerfile true }
  
  stages {
    stage('Dockerbuild') {
      steps {
        checkout scm
        app = docker.build('webflux-r2dbc-demo:${env.BUILD_ID}')
      }
    }
  }
}
