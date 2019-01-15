#pragma once
#ifndef EXPORTED_H
#define EXPORTED_H

#include <string>

#ifdef NVRFETCH_EXPORTS
	#define NVRFETCH_API __declspec(dllexport)
#else
	#define NVRFETCH_API __declspec(dllimport)
#endif

extern "C" NVRFETCH_API unsigned char* get_frame_c(std::string url);
extern "C" NVRFETCH_API void release_frame_c();

#endif
