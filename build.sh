#!/bin/bash

# navigate to directory
old_dir=$(pwd)
cd $1

# save
git add .
git commit -m "[auto] build save"
git push

# setup directories to look for
script_home=$(pwd)
script_build="$hvh_home/build"
script_dist="$hvh_home/dist"
script_src="$hvh_home/src"
script_lib="$hvh_home/lib"

# compile
#

cd $script_src

# find files (.java)
src_files=$(find . -name "*.java") 
echo -en "\e[33m"
echo -e "$src_files" | awk '{ print "compiling... " $0 }'
echo -en "\e[32m"

# add config to jar (plugin.yml)
jar -cf "$script_dist/Locations.jar" plugin.yml

# create .class files
javac -cp $script_lib/*.jar $src_files -d $hvh_build


# compress
#

cd $script_build

# create .jar
class_files=$(find . -name "*.class")
echo -en "\e[33m"
echo -e "$src_files" | awk '{ print "compressing... " $0 }'
echo -en "\e[32m"

jar -uf "$script_dist/Locations.jar" $class_files
echo -en "\e[37m"

cd $old_dir
