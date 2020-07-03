![KCL](https://github.com/kasisoft/kcl/workflows/KCL/badge.svg?branch=master&event=push)

# Purpose

This library is a simple collection of useful utility functions that grew over the past. It doesn't serve a specific 
purpose other than providing some handy functionalities.


# Infos

* [eMail: daniel.kasmeroglu@kasisoft.com](mailto:daniel.kasmeroglu@kasisoft.com)
* [Github](https://github.com/kasisoft/kcl/issues)
* [Jenkins](https://kasisoft.com/jenkins/job/libs.com.kasisoft.libs.common)
* [GIT](https://github.com/kasisoft/kcl.git)

https://kasisoft.github.io/kcl/

# Development Setup

I assume that you're familiar with Maven. If not I suggest to visit the following page:

* https://maven.apache.org/


## Requirements

* Java 14


## Maven

### Releases

     <dependency>
         <groupId>com.kasisoft</groupId>
         <artifactId>com.kasisoft.libs.common</artifactId>
         <version>3.5</version>
     </dependency>


### Snapshots

Snapshots can be used while accessing a dedicated maven repository. Your POM needs the following settings:

     <dependency>
         <groupId>com.kasisoft</groupId>
         <artifactId>com.kasisoft.libs.common</artifactId>
         <version>3.6-SNAPSHOT</version>
     </dependency>
     
     <repositories>
         <repository>
             <id>gh-kasisoft</id>
             <url>https://maven.pkg.github.com/kasisoft/kcl</url>
             <releases>
                 <enabled>false</enabled>
             </releases>
             <snapshots>
                 <enabled>true</enabled>
             </snapshots>
         </repository>
     </repositories>
     

Add the following credentials to your ~/.m2/settings.xml:

     <server>
         <id>gh-kasisoft</id>
         <username>kasisoft</username>
         <password>a7c05e559dab4dc35c2b2a7750db64dadeb576f2</password>
     </server>
    
     

# License

MIT License

Copyright (c) 2020 Daniel Kasmeroglu (Kasisoft)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
