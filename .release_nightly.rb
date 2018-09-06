# Copyright 2018 Gergő Pintér
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

require 'rest-client'
require 'json'

if ENV["TRAVIS_EVENT_TYPE"] != "cron" then
    exit true
end

def create_release
    release_msg = "Nightly build based on #{$commit} at #{Time.now.strftime("%Y-%m-%d %H:%M:%S")}"
    response = JSON.parse(RestClient.post "https://api.github.com/repos/#{$owner}/#{$repo}/releases?access_token=#{$GH_TOKEN}", {tag_name: $tag, target_commitish: $branch, name: "nightly", body: release_msg, draft: false, prerelease: true}.to_json)
    
    RestClient::Request.execute(method: :post, url: response["upload_url"][0..-14] + "?name='#{$filename_label}'&label=Single runnable jar&access_token=#{$GH_TOKEN}", payload: File.new($filename, "rb"), headers: {'Content-Type': 'application/zip'})
    
    puts "release added"
end

# set variables
$owner = "SzFMV2018-Tavasz"
$repo = "AutomatedCar"
$branch = "master"
$tag = "nightly"
$filename = "./target/AutomatedCar-jar-with-dependencies.jar"
$filename_label="AutomatedCar.jar"
$GH_TOKEN = ENV["GH_TOKEN"] # set through Travis
$commit = %x(git log --format=%H -1) # this gives the full commit hash, %h is the short
$user_email = ENV["USER_EMAIL"] # set through Travis
$user_name = ENV["USER_NAME"] # set through Travis


%x(git clone --quiet "https://#{ENV["USER_NAME"]}:#{ENV["GH_TOKEN"]}@github.com/#{$owner}/#{$repo}" #{ENV["HOME"]}/#{$branch} > /dev/null)

Dir.chdir(ENV["HOME"] + "/" + $branch){
    %x(git config user.email "#{ENV["USER_EMAIL"]}")
    %x(git config user.name "#{ENV["USER_NAME"]}")
    
    # build
    puts "initiating build..."
    exit_code = system "mvn clean compile assembly:single"
    if exit_code == false then
        puts "The build has failed!"
        exit false
    end

    begin
        puts "deleting previous release..."
        # delete release
        get = JSON.parse(RestClient.get "https://api.github.com/repos/#{$owner}/#{$repo}/releases/tags/#{$tag}?access_token=#{$GH_TOKEN}")
        RestClient.delete "https://api.github.com/repos/#{$owner}/#{$repo}/releases/#{get["id"]}?access_token=#{$GH_TOKEN}"
        
        # delete tag: deleting the GitHub release will not delete the tag on which the release is based
        # delete from remote
        %x(git push --delete origin #{$tag})
        # delete from local
        %x(git tag -d #{$tag})
        %x(git push -q origin :refs/tags/#{$tag} > /dev/null)
        
        # creating a release seems to create a tag first...
        create_release
    rescue RestClient::ExceptionWithResponse => err
        case err.http_code
        when 404 # if there was no release with the given name, this is the first time this script is run in a repository
            create_release
        else
            raise
        end
    end
}
