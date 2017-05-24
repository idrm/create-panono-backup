## Panono panorama backup tool

You will need to install Java 8+ in order to use this app to download your Panono panoramas onto your computer.

The included backups will contain the equirecetangular image set that Panono creates as well as the UPF package that 
stores the individual JPGs that comprise each panorama.

### Download

[create-panono-backup-1.0.jar](http://radiatic.com/create-panono-backup-1.0.jar)

### Usage in Windows

Make sure your Java installation is in your command prompt path. To test this, enter `java` in a command prompt, 
press the Enter key, and if you see a diagnostic message showing you the version of Java you have installed, you're golden.

Copy the `create-panono-backup-1.0.jar` file to a folder where you want to store your panoramas. Let's say that folder is `C:\PanonoBackups`.

Open a command prompt window, and enter `cd C:\PanonoBackups`.

Now type `java -jar create-panono-backup-1.0.jar --username "USERNAME" --password "PASSWORD" --includeUpf UPFFLAG`, replacing 
`USERNAME` with your username, `PASSWORD` with your password, and `UPFFLAG` with either `yes` or `no', depending upon whether 
you want to download the UPF packages. Keep the double quotes as part of the command arguments so that any blank space that may
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

Copy the `create-panono-backup-1.0.jar` file to a folder where you want to store your panoramas. Let's say that folder 
is `/Users/jack/PanonoBackups`.

Open a terminal window, and type `cd /Users/jack/PanonoBackups`.

Now type `java -jar create-panono-backup-1.0.jar --username "USERNAME" --password "PASSWORD" --includeUpf UPFFLAG`, replacing 
`USERNAME` with your username, `PASSWORD` with your password, and `UPFFLAG` with either `yes` or `no`, depending upon whether 
you want to download the UPF packages. Keep the double quotes as part of the command arguments so that any blank space that may
be part of your password (or username) are handled correctly.

The app will create a `panono-backups` sub-folder inside `C:\PanonoBackups`, as well as a sub-folder that has the same name
as your username. It will load up your panoramas' metadata (title, description, etc), and one by one create a sub-folder 
for each panorama and save the metadata as well as all image data there.

Each panorama folder (and the files in it) will be named with the ID of the panorama, which may appear cryptic, but is meant
to ensure only legal characters are used. Since panorama title may include non-filename-legal character (e.g. an asterisk), 
those cannot be used to name the files. Instead, the title, the description, and the creation date are stored in a txt file 
inside of the panorama folder.

### Notes

A UPF package is the set of images that the Panono camera ball takes for each panorama. You can extract the images located 
in a UPF, and use 3rd party stitching software (e.g. PTGui, PanoramaStudio, etc.) to create your own panoramas. A tutorial
on how to do that is available 
[here](http://360rumors.com/2017/05/exclusive-stitch-panono-images-offline-fix-panono-stitching-errors.html).

### Disclaimer

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

### License

The source code is licensed under the Apache 2 license.