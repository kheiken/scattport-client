ScattPort - Client for light scattering simulators
========================================================

This client runs on a machine that handles the actual calculations.
Currently the only supported application is [SScaTT][sscatt].

[sscatt]: http://www.scattport.org/index.php/programs-menu/generalized-multipole-menu/50-sscatt-program "Superellipsoid Scattering Tool"

Compilation
------------

### tl;dr

    $ sudo apt-get install ant
    $ git clone git://github.com/krstn/scattport-client.git
    $ cd scattport-client
    $ ant
    $ ant jar
    $ cp dist/lib/libsigar-amd64-linux.so /usr/lib/

### Complete Walkthrough
The easiest way to compile this application is to use ant.
There is an ant buildfile included.

To compile ScattPortClient, simply issue the command "ant".

To create a runnable jar, you can use "ant jar".

ScattPortClient uses the library SIGAR to access some system information.
For SIGAR to be able to run, you need to copy the corresponding library into your system.

If you are running \*nix, the easiest way is to copy lib/libsigar-*-linux.so to /usr/lib/.

License
-------

This application is published under the MIT License. See LICENSE.

This application uses the following third-party software:

* SimpleFTP, http://www.jibble.org/simpleftp/
* SIGAR, http://www.hyperic.com/products/sigar
