node {
    stage('Dockerbuild') {
        checkout scm
        app = docker.build('webflux-r2dbc-demo:${env.BUILD_ID}')
    }
}
