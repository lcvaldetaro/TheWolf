# NOTE: This version of Adventure was developed by the author on private
# equipment, and has been ported to Android by Luiz Valdetaro for entertainment purposes only.
# The author (Don Woods) retains full rights to the work.

# This make file is intended for use with Windows as the cross-compile host system.
# To compile this you must install the android NDK, and must modify the window's PATH environment variable. As an example, this is what I did:
# set path=c:\android-ndk\toolchains\arm-linux-androideabi-4.4.3\prebuilt\windows\bin;%path%
# set path=c:\android-sdk\platform-tools;%path%

# NDK should point to the root of the Android NDK
NDK=\android-ndk

# TOOL should point to the toolchain used - it must be in the path
TOOL=$(NDK)\toolchains\arm-linux-androideabi-4.4.3\prebuilt\windows\bin

COMP=arm-linux-androideabi-gcc
LINK=arm-linux-androideabi-ld
COPY=cp

# SYSROOT points to the top of include directories - in this case for android API 8 (Android 2.2)
SYSROOT=/android-ndk/platforms/android-8/arch-arm
LIB=$(SYSROOT)/usr/lib


OBJS = objs.o misc.o id_ca.o id_vh.o id_us.o \
	wl_act1.o wl_act2.o wl_act3.o wl_agent.o wl_game.o \
	wl_inter.o wl_menu.o wl_play.o wl_state.o wl_text.o wl_main.o \
	wl_debug.o vi_comm.o sd_comm.o \
        wl_draw.o \
        sd_null.o \
        jni_wolf.o vi_null.o


.c.o:
	$(COMP) -g -c --sysroot=$(SYSROOT) -fPIC -DLINUX -DANDROID -DSHARED_LIBRARY $<

libwolf_jni.so:	$(OBJS)
	$(LINK) -shared -o libwolf_jni.so --dynamic-linker=/system/bin/linker -nostdlib -rpath /system/lib -L. -rpath $(SYSROOT)/lib -L$(SYSROOT)/lib -rpath $(LIB) -L $(LIB) -lc -lm -ldl -llog -lz $(OBJS)
	$(COPY) libwolf_jni.so ../../libs/armeabi



wolf.jni_Natives.h: ../../bin/classes/wolf/jni/Natives.class
	javah -jni -classpath ../../bin/classes -d . wolf.jni.Natives


