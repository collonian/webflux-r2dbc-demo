node {
    script {
                GIT_COMMIT_HASH = sh "(git log -1 --pretty=format:%h)"

                echo "**************************************************"
                echo "${GIT_COMMIT_HASH}"
                echo "**************************************************"
    }
    
    stage('Dockerbuild') {
        checkout scm
        app = docker.build('webflux-r2dbc-demo:$BUILD_NUMBER')
    }
}
