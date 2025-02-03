# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

set (base-dir ${PROJECT_SOURCE_DIR})
set (lib-files "/src/main/cpp/")
set (c-files "src/main/cpp/")

#include directories
include_directories(${c-files})


#dynamic libraries to be used
find_library( log-lib log )
find_library( m-lib m )
find_library( z-lib z )

SET (CMAKE_C_FLAGS  "${CMAKE_C_FLAGS} -DLINUX -DANDROID -DANDROID_STUDIO -DSHARED_LIBRARY -falign-functions=16 -Wno-implicit-function-declaration -Wno-implicit-int -Wno-return-type -Wno-pointer-sign -Wno-empty-body -Wno-comment -W-notautological-constant-out-of-range-compare -Wno-incompatible-pointer-types -Wno-format -Wno-absolute-value -Wno-pointer-to-int-cast" )
SET (CMAKE_C_FLAGS  "${CMAKE_C_FLAGS} ${GCC_COMPILATION_FLAGS}" )


set (lib-wolf "cacau.wolf")
set (dir-wolf "")

add_library( ${lib-wolf}
        SHARED
        ${c-files}${dir-wolf}/objs.c
        ${c-files}${dir-wolf}/misc.c
        ${c-files}${dir-wolf}/id_ca.c
        ${c-files}${dir-wolf}/id_vh.c
        ${c-files}${dir-wolf}/id_us.c
        ${c-files}${dir-wolf}/wl_act1.c
        ${c-files}${dir-wolf}/wl_act2.c
        ${c-files}${dir-wolf}/wl_act3.c
        ${c-files}${dir-wolf}/wl_agent.c
        ${c-files}${dir-wolf}/wl_game.c
        ${c-files}${dir-wolf}/wl_inter.c
        ${c-files}${dir-wolf}/wl_menu.c
        ${c-files}${dir-wolf}/wl_play.c
        ${c-files}${dir-wolf}/wl_state.c
        ${c-files}${dir-wolf}/wl_text.c
        ${c-files}${dir-wolf}/wl_main.c
        ${c-files}${dir-wolf}/wl_debug.c
        ${c-files}${dir-wolf}/vi_comm.c
        ${c-files}${dir-wolf}/sd_comm.c
        ${c-files}${dir-wolf}/wl_draw.c
        ${c-files}${dir-wolf}/sd_null.c
        ${c-files}${dir-wolf}/vi_null.c
        ${c-files}${dir-wolf}/jni_wolf.c
        )
target_link_libraries(${lib-wolf} ${log-lib} )
target_link_libraries(${lib-wolf} ${m-lib} )
target_link_options(${lib-wolf} PRIVATE "-Wl,-z,max-page-size=16384")
