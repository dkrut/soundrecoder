# SoundRecoder
_sound recording on a microphone and sending to dropbox_

A local microphone is used where the code is executed. One recording time is 60 seconds. File name consists of current datetime.

To upload recording to Dropbox, you should have dropbox access token. How to get you can read [here](https://github.com/dropbox/dropbox-sdk-java).

Then, fill the _app.properties_: dropbox access token and iterations count - how many times it will make recording and send file to Dropbox