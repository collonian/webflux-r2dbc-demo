node {
    stage('Dockerbuild') {
        checkout scm
        app = docker.build('webflux-r2dbc-demo:$BUILD_NUMBER')
    }
}
