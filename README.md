## Panono panorama backup tool

You will need to install Java 8+ in order to use this app to download your Panono panoramas onto your computer.

The included backups will contain the equirecetangular image set that Panono creates as well as the UPF package that 
stores the individual JPGs that comprise each panorama (assuming you elect to download these).

### Java installation

If you do not have Java 8 SDK already installed, you can download it [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

Once you have installed Java you have to add it to your PATH system variable. [This tutorial](https://www.java.com/en/download/help/path.xml) explains how to do that.

### Download

[create-panono-backup-1.2.jar](http://radiatic.com/create-panono-backup-1.2.jar)

(Please, exercise caution and scan this Java executable file for malware before running it. The SHA-1 hash of the downloaded 
file should be 247e95806fc5786b69e0ae34bfb0750ad45f06b0)

### Usage in Windows

Make sure your Java installation is in your command prompt path. To test this, enter `java` in a command prompt, 
press the Enter key, and if you see a diagnostic message showing you the version of Java you have installed, you're golden.

Copy the `create-panono-backup-1.2.jar` file to a folder where you want to store your panoramas. Let's say that folder is `C:\PanonoBackups`.

Open a command prompt window, and enter `cd C:\PanonoBackups`.

Now type `java create-panono-backup-1.2.jar --username "USERNAME" --password "PASSWORD" --includeUpf INCLUDE_UPF_FLAG --timestampedFolders TIMESTAMPED_FOLDERS_FLAG`, replacing 
`USERNAME` with your username, `PASSWORD` with your password, `INCLUDE_UPF_FLAG` with either `yes` or `no`, depending upon whether 
you want to download the UPF packages, and `TIMESTAMPED_FOLDERS_FLAG` with either `yes` or `no` depending upon if you'd like to prepend each folder
with a timestamp. Keep the double quotes as part of the command arguments so that any blank space that may
be part of your password (or username) are handled correctly.

The app will create a `panono-backups` sub-folder inside `C:\PanonoBackups`, as well as a sub-folder that has the same name
as your username. It will load up your panoramas' metadata (title, description, etc), and one by one create a sub-folder 
for each panorama and save the metadata as well as all image data there.

Each panorama folder (and the files in it) will be named with the ID of the panorama, which may appear cryptic, but is meant
to ensure only legal characters are used. Since panorama title may include non-filename-legal character (e.g. an asterisk), 
those cannot be used to name the files. Instead, the title, the description, and the creation date are stored in a txt file 
inside of the panorama folder.

### Usage in OS X (and other Unix flavors)

Ensure that you have Java 8+ installed by entering `java` in a terminal window.

Copy the `create-panono-backup-1.2.jar` file to a folder where you want to store your panoramas. Let's say that folder 
is `/Users/jack/PanonoBackups`.

Open a terminal window, and type `cd /Users/jack/PanonoBackups`.

Now type `java create-panono-backup-1.2.jar --username "USERNAME" --password "PASSWORD" --includeUpf INCLUDE_UPF_FLAG --timestampedFolders TIMESTAMPED_FOLDERS_FLAG`, replacing 
`USERNAME` with your username, `PASSWORD` with your password, `INCLUDE_UPF_FLAG` with either `yes` or `no`, depending upon whether 
you want to download the UPF packages, and `TIMESTAMPED_FOLDERS_FLAG` with either `yes` or `no` depending upon if you'd like to prepend each folder
with a timestamp. Keep the double quotes as part of the command arguments so that any blank space that may
be part of your password (or username) are handled correctly.

The app will create a `panono-backups` sub-folder inside `/Users/jack/PanonoBackups`, as well as a sub-folder that has the same name
as your username. It will load up your panoramas' metadata (title, description, etc), and one by one create a sub-folder 
for each panorama and save the metadata as well as all image data there.

Each panorama folder (and the files in it) will be named with the ID of the panorama, which may appear cryptic, but is meant
to ensure only legal characters are used. Since panorama title may include non-filename-legal character (e.g. an asterisk), 
those cannot be used to name the files. Instead, the title, the description, and the creation date are stored in a txt file 
inside of the panorama folder.

### Notes

All timestamps used in the app are in UTC.

A UPF package is the set of images that the Panono camera ball takes for each panorama. You can extract the images located 
in a UPF, and use 3rd party stitching software (e.g. PTGui, PanoramaStudio, etc.) to create your own panoramas. A tutorial
on how to do that is available 
[here](http://360rumors.com/2017/05/exclusive-stitch-panono-images-offline-fix-panono-stitching-errors.html).

The current version of the backup tool will not save your album data. It will only download the individual panoramas.

Each run of the app will download only panoramas that it has not downloaded before (assuming the destination folder is the same).
If you change the `includeUpf` flag between runs it will not sync the change for already downloaded panoramas. 

Be aware that each panorama can be anywhere from ~35M to ~140MB, depending upon if you're downloading the UPF packages.

This tool uses reverse-engineered web API calls to Panono's servers. If Panono change their API, the tool may stop working, 
requiring an update to make it compatible again.

### Would-be-nice-to-have list

- Avoid having to deal with the Java setup process
- Enable downloading of cubemap images
- Enable downloading of album data
- Where possible, use the panorama title as the file/folder name

### Changelog

#### 1.2

- Added an option to have each panorama's folder name be prepended with the date and the time the panorama was created at.
If you have previously downloaded panoramas using this app and enable this flag during a new run, all those panoramas' folders will be
renamed to include the timestamp. If you run the app with this flag turned off after having previously run it
with it enabled you will get duplicate downloads (i.e. once you run the backup tool with this flag set to `yes`, always stick with
that setting)

- Files are now saved using streams, significantly reducing memory requirements

#### 1.1.2

- Fixed a bug that prevented usernames containing "@" from working correctly

#### 1.1.1

- Prevent the app from crashing during an incremental backup

#### 1.1

- Enabled incremental backups

### Disclaimer

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Aka, use it at your own risk.

### License

The source code is licensed under the Apache 2 license.