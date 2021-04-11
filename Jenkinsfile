node {
    script {
                GIT_COMMIT_HASH = sh "(git log -n 1 --pretty=format:'%H')"

                echo "**************************************************"
                echo "${GIT_COMMIT_HASH}"
                echo "**************************************************"
    }
    
    stage('Dockerbuild') {
        checkout scm
        app = docker.build('webflux-r2dbc-demo:$BUILD_NUMBER')
    }
}
