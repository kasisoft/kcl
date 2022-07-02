![KCL](https://github.com/kasisoft/kcl/workflows/KCL/badge.svg?branch=master&event=push)

# Purpose

This library is a simple collection of useful utility functions that grew over the past. It doesn't serve a specific 
purpose other than providing some handy functionalities.


# Infos

* [daniel.kasmeroglu@kasisoft.com](mailto:daniel.kasmeroglu@kasisoft.com)
* [Github](https://github.com/kasisoft/kcl/issues)
* [GIT](https://github.com/kasisoft/kcl.git)


# Development Setup

The build uses Maven

* [https://maven.apache.org/](https://maven.apache.org/)


## Requirements

* Java 11


## Maven

### Releases

     <dependency>
         <groupId>com.kasisoft</groupId>
         <artifactId>com.kasisoft.libs.common</artifactId>
         <version>4.4</version>
     </dependency>


### Snapshots

Snapshots can be used while accessing a dedicated maven repository. Your POM needs the following settings:

     <dependency>
         <groupId>com.kasisoft</groupId>
         <artifactId>com.kasisoft.libs.common</artifactId>
         <version>4.5-SNAPSHOT</version>
     </dependency>
     
     <repositories>
         <repository>
             <id>github-kcl</id>
             <url>https://maven.pkg.github.com/kasisoft/kcl</url>
             <releases>
                 <enabled>false</enabled>
             </releases>
             <snapshots>
                 <enabled>true</enabled>
             </snapshots>
         </repository>
     </repositories>

You need to provide credentials in your _~/.m2/settings.xml_:

     <server>
         <id>github-kcl</id>
         <username>username</username>
         <password>token</password>
     </server>

You can create a token with your github account:

* Open your Github settings
* Select _Developer settings_
* Select _Personal access tokens_
* Create a new token with read:package access



# License

MIT License

Copyright (c) 2022 Daniel Kasmeroglu (Kasisoft)

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

----

Generics: suffix ST = SameTypes
