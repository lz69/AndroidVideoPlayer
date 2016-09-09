LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := main.c
LOCAL_LDLIBS += -llog
LOCAL_MODULE := test

include $(BUILD_SHARED_LIBRARY)