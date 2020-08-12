#!/bin/sh
export JAVA_HOME=/Applications/Android\ Studio.app/Contents/jre/jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
gradle app:assemble

aar=/Users/Wuquancheng/Documents/workspace/code/mini-studio/rdec/rdec-android/app/libs/mini-frame-1.0.0.aar
rm -fr ${aar} 
cp app/build/outputs/aar/mini_frame_debug_1.0.aar ${aar}
echo 'sucess'
