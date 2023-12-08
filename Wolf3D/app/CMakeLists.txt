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

SET (CMAKE_C_FLAGS  "${CMAKE_C_FLAGS} -DLINUX -DANDROID -DANDROID_STUDIO -DSHARED_LIBRARY -Wno-implicit-function-declaration -Wno-implicit-int -Wno-return-type -Wno-pointer-sign -Wno-empty-body -Wno-comment -W-notautological-constant-out-of-range-compare " )
SET (CMAKE_C_FLAGS  "${CMAKE_C_FLAGS} ${GCC_COMPILATION_FLAGS}" )


set (lib-adv "cacau.wolf")
set (dir-adv "")

add_library( ${lib-adv}
        SHARED
        ${c-files}${dir-adv}/objs.c
        ${c-files}${dir-adv}/misc.c
        ${c-files}${dir-adv}/id_ca.c
        ${c-files}${dir-adv}/id_vh.c
        ${c-files}${dir-adv}/id_us.c
        ${c-files}${dir-adv}/wl_act1.c
        ${c-files}${dir-adv}/wl_act2.c
        ${c-files}${dir-adv}/wl_act3.c
        ${c-files}${dir-adv}/wl_agent.c
        ${c-files}${dir-adv}/wl_game.c
        ${c-files}${dir-adv}/wl_inter.c
        ${c-files}${dir-adv}/wl_menu.c
        ${c-files}${dir-adv}/wl_play.c
        ${c-files}${dir-adv}/wl_state.c
        ${c-files}${dir-adv}/wl_text.c
        ${c-files}${dir-adv}/wl_main.c
        ${c-files}${dir-adv}/wl_debug.c
        ${c-files}${dir-adv}/vi_comm.c
        ${c-files}${dir-adv}/sd_comm.c
        ${c-files}${dir-adv}/wl_draw.c
        ${c-files}${dir-adv}/sd_null.c
        ${c-files}${dir-adv}/vi_null.c
        ${c-files}${dir-adv}/jni_wolf.c
        )
target_link_libraries(${lib-adv} ${log-lib} )
target_link_libraries(${lib-adv} ${m-lib} )

