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
    $ ant jar
    $ cp dist/lib/libsigar-$YOUR-ARCHITECTURE-linux.so /usr/lib/

### Complete Walkthrough

See [Installation][installation].

[installation]: https://github.com/kheiken/scattport-client/wiki/Installation "Installation"

License
-------

This application is published under the MIT License. See LICENSE.

This application uses the following third-party software:

* SimpleFTP, http://www.jibble.org/simpleftp/
* SIGAR, http://www.hyperic.com/products/sigar
