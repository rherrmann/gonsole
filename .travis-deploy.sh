if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  echo -e "Starting to deploy to gh-pages\n"

  # create and cd into temporary deployment work directory
  mkdir deployment-work
  cd deployment-work
  
  # setup git and clone from gh-pages branch
  git config --global user.email "travis-deployer@codeaffine.com"
  git config --global user.name "Travis Deployer"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/rherrmann/gonsole.git . > /dev/null 2>&1
echo -e "after clone:\n"
ls -la

  # copy the build result into the gh-pages repository
  mkdir -p repository 
  cp -Rf ../com.codeaffine.gonsole.releng/repository/target/repository/* ./repository
  # add, commit and push files
  git add -f .
echo -e "status after add:\n"
git status
  git commit -m "Deploy Travis build #$TRAVIS_BUILD_NUMBER to gh-pages"
  git push -fq origin gh-pages > /dev/null 2>&1

  # go back to the directory where we started
  cd..
  
  echo -e "Done with deployment to gh-pages\n"
fi