##
CC = g++

CFLAGS = -O3 -ffast-math -funroll-loops -std=c++11 -mavx -fopenmp -fPIC


CPPSRCS  = $(wildcard PostToolForECSympoCompe/*.cpp)
CPPSRCS += $(wildcard PostToolForECSympoCompe/json11/json11.cpp)

TMPOBJS  = $(patsubst %.cpp, %.o, $(notdir $(wildcard PostToolForECSympoCompe/*.cpp)))
TMPOBJS += $(patsubst %.cpp, %.o, $(notdir $(wildcard PostToolForECSympoCompe/json11/json11.cpp)))
OBJDIR = Objectfiles
CPPOBJS  = $(addprefix $(OBJDIR)/, $(TMPOBJS))

CPPHDRS = $(wildcard PostToolForECSympoCompe/*.h)
CPPHDRS += $(wildcard PostToolForECSympoCompe/json11/json11.hpp)

PROGRAM = ReleaseLinux/PostToolForECSympoCompe.exe

all:
	$(CC) $(CFLAGS) $(CPPSRCS) -o $(PROGRAM)


