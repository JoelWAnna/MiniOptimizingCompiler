#!/bin/bash
TestFiles=(`ls *.mini`)
dir="Output/"
mkdir -p $dir
len=${#TestFiles[*]}
i=0

rm mil/MILProgram.class
rm mil/MILProgram.java
cp mil/MILProgram.java.orig mil/MILProgram.java
javac  mini/*.java mil/MILProgram.java
while [ $i -lt $len ]; do
java mini.MilGen ${TestFiles[$i]} > ${dir}${TestFiles[$i]}.output.orig 2>&1
mv before.dot ${dir}${TestFiles[$i]}.before.orig.dot
mv after.dot ${dir}${TestFiles[$i]}.after.orig.dot
let i++
done
i=0
rm mil/MILProgram.class
rm mil/MILProgram.java
cp mil/MILProgram.java.global mil/MILProgram.java
javac  mini/*.java mil/MILProgram.java
while [ $i -lt $len ]; do
java mini.MilGen ${TestFiles[$i]} > ${dir}${TestFiles[$i]}.output.global 2>&1
mv before.dot ${dir}${TestFiles[$i]}.before.global.dot
mv after.dot ${dir}${TestFiles[$i]}.after.global.dot
let i++
done
