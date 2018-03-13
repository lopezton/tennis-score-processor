build:
	mvn install

travis-deploy:
	gpg --import keys/private-key.gpg
	mvn versions:set -DnewVersion=${TRAVIS_TAG}
	mvn clean deploy -P release --settings settings.xml