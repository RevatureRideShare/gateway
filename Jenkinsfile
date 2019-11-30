pipeline {

    agent any
    
    triggers {
    	pollSCM('') // Enabling being build on Push
  	}
  	
  	options{
  		disableConcurrentBuilds()
  		buildDiscarder(logRotator(numToKeepStr: '10'))
  	}

    stages {

        stage ('Build') {
            steps {
                
                deleteDir()
                checkout scm
                
                // Run in non-interactive (batch) mode
                sh 'mvn -U -B -DskipTests clean package'
            
            }
        }

        stage ('Test') {
            steps {
            	sh 'mvn verify checkstyle:checkstyle'
            	withSonarQubeEnv(credentialsId: 'b44ffadc-08d5-11ea-8d71-362b9e155667', installationName:'SonarCloud'){
                	sh '''
                    export SONAR_SCANNER_VERSION=4.2.0.1873
                    export SONAR_SCANNER_HOME=$HOME/.sonar/sonar-scanner-$SONAR_SCANNER_VERSION-linux
                    export PATH=$SONAR_SCANNER_HOME/bin:$PATH
                    export SONAR_SCANNER_OPTS="-server"
                    
                    sonar-scanner \
                    -Dsonar.projectKey=RevatureRideShare_gateway \
                    -Dsonar.organization=b44ffadc-08d5-11ea-8d71-362b9e155667 \
                    -Dsonar.sources=. \
                    -Dsonar.host.url=https://sonarcloud.io/ \
                    -Dsonar.login=f8aed430569e55940896c6f7c967d6a05efca071 \
                    -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }
        
  		stage ('QualityGate') {
  			steps{
  				timeout(time: 1, unit: 'MINUTES'){
  					waitForQualityGate abortPipeline: true
  				}
  			}
  		}
        

        stage ('Deploy') {
            steps {
                withCredentials([[$class          : 'UsernamePasswordMultiBinding',
                                  credentialsId   : 'PCF_LOGIN',
                                  usernameVariable: 'USERNAME',
                                  passwordVariable: 'PASSWORD']]) {
                    sh 'cf events rideshare-security-service'
					sh 'cf logs rideshare-security-service --recent'
                    sh 'cf login -a http://api.run.pivotal.io -u $USERNAME -p $PASSWORD \
                    -o "Revature Training" -s development'
                    sh 'cf push'
                }
            }
        }
    }
        
     post{
       	always{
        	deleteDir()
        }
    }
}
