#include "com_lz69_androidvideoplayer_jni_Test.h"

JNIEXPORT jstring JNICALL Java_com_lz69_androidvideoplayer_jni_Test_test(JNIEnv * env, jclass clazz)
{
    return (*env)->NewStringUTF(env, "Hello from JNI!");
}

